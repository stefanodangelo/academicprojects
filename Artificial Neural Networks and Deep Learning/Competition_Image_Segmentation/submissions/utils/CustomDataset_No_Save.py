from PIL import Image
from read_mask_from_array import read_rgb_mask
import os
import tensorflow as tf
import numpy as np

SEED = 1234

class CustomDataset(tf.keras.utils.Sequence):
    """
    CustomDataset inheriting from tf.keras.utils.Sequence.

    3 main methods:
      - __init__: save dataset params like directory, filenames..
      - __len__: return the total number of samples in the dataset
      - __getitem__: return a sample from the dataset

    Note: 
      - the custom dataset return a single sample from the dataset. Then, we use 
        a tf.data.Dataset object to group samples into batches.
      - in this case we have a different structure of the dataset in memory. 
        We have all the images in the same folder and the training and validation splits
        are defined in text files.

    """

    def __init__(self, dataset_dir, which_subset, which_dataset, tiles_creator, img_generator=None, mask_generator=None, 
               preprocessing_function=None, out_shape=[256, 256]):

        self.which_subset = which_subset
        self.which_dataset = which_dataset
        self.dataset_dir = dataset_dir
        self.img_generator = img_generator
        self.mask_generator = mask_generator
        self.preprocessing_function = preprocessing_function
        self.out_shape = out_shape
        self.tiles_creator = tiles_creator

    def __len__(self):
        return len(self.tiles_creator.get_images())

    def __getitem__(self, index):
        
        # Read Image
        images = self.tiles_creator.get_images()
        masks = self.tiles_creator.get_masks()

        img_arr = images[index]
        mask_arr = masks[index]

        mask_arr = read_rgb_mask(mask_arr)

        mask_arr = tf.image.rgb_to_grayscale(mask_arr)
        
        if self.img_generator is not None and self.mask_generator is not None:
            # Perform data augmentation
            # We can get a random transformation from the ImageDataGenerator using get_random_transform
            # and we can apply it to the image using apply_transform
            img_t = self.img_generator.get_random_transform(img_arr.shape, seed=SEED)
            mask_t = self.mask_generator.get_random_transform(mask_arr.shape, seed=SEED)
            img_arr = self.img_generator.apply_transform(img_arr, img_t)
            # ImageDataGenerator use bilinear interpolation for augmenting the images.
            # Thus, when applied to the masks it will output 'interpolated classes', which
            # is an unwanted behaviour. As a trick, we can transform each class mask 
            # separately and then we can cast to integer values (as in the binary segmentation notebook).
            # Finally, we merge the augmented binary masks to obtain the final segmentation mask.
            out_mask = np.zeros_like(mask_arr)
            
            for c in np.unique(mask_arr):
                if c > 0:
                    curr_class_arr = np.float32(mask_arr == c)
                    curr_class_arr = self.mask_generator.apply_transform(curr_class_arr, mask_t)
                    # from [0, 1, 2] to {0, 1, 2}
                    curr_class_arr = np.uint8(curr_class_arr)
                    # recover original class
                    curr_class_arr = curr_class_arr * c 
                    out_mask += curr_class_arr
        else:
            out_mask = mask_arr

        if self.preprocessing_function is not None:
            img_arr = self.preprocessing_function(img_arr)
        
        return img_arr, np.float32(out_mask)