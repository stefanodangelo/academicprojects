from PIL import Image
from read_mask_from_txt import read_rgb_mask
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

    def __init__(self, dataset_dir, which_subset, which_dataset, img_generator=None, mask_generator=None, 
               preprocessing_function=None, out_shape=[256, 256]):

        self.which_subset = which_subset
        self.which_dataset = which_dataset
        if which_subset == 'Training' or which_subset == 'Validation':
          self.filenames = os.listdir(os.path.join(dataset_dir, which_subset, which_dataset, 'cropped_images'))
        else:  
          self.filenames = os.listdir(os.path.join(dataset_dir, which_subset, which_dataset, 'Images'))
        self.dataset_dir = dataset_dir
        self.img_generator = img_generator
        self.mask_generator = mask_generator
        self.preprocessing_function = preprocessing_function
        self.out_shape = out_shape

    def __len__(self):
        return len(self.filenames)

    def __getitem__(self, index):
        
        if self.which_subset == 'Training' or self.which_subset == 'Validation':
            # Read Image
            curr_filename = self.filenames[index]
            curr_filename = curr_filename[:-4]
            img_path = os.path.join(self.dataset_dir, self.which_subset, self.which_dataset, 'cropped_images', curr_filename + '.jpg')
            mask_path = os.path.join(self.dataset_dir, self.which_subset, self.which_dataset, 'cropped_masks', curr_filename + '.png')

            img = Image.open(img_path)
            mask = Image.open(mask_path)

            img_arr = np.array(img)
            
            mask_arr = read_rgb_mask(mask_path)

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
        else:
            # Read Image
            curr_filename = self.filenames[index]
            curr_filename = curr_filename[:-4]
            img_path = os.path.join(self.dataset_dir, self.which_subset, self.which_dataset, 'Images', curr_filename + '.jpg')
            mask_path = os.path.join(self.dataset_dir, self.which_subset, self.which_dataset, 'Masks', curr_filename + '.png')

            img = Image.open(img_path)
            mask = Image.open(mask_path)

            # Resize image and mask
            img = img.resize(self.out_shape)
            mask = mask.resize(self.out_shape, resample=Image.NEAREST)

            img_arr = np.array(img)
            mask_arr = read_rgb_mask(mask_path, self.out_shape)

            mask_arr = tf.image.rgb_to_grayscale(mask_arr)
            out_mask = mask_arr

        if self.preprocessing_function is not None:
            img_arr = self.preprocessing_function(img_arr)
        
        return img_arr, np.float32(out_mask)