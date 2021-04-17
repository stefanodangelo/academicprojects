from PIL import Image
from tiling import ConstSizeTiles
import tensorflow as tf
import numpy as np
import os

class TilesCreator(tf.keras.utils.Sequence):
    
    def __init__(self, dataset_dir, which_subset, which_dataset, out_shape):

        self.which_subset = which_subset
        self.which_dataset = which_dataset
        self.filenames = os.listdir(os.path.join(dataset_dir, which_subset, which_dataset, 'Images'))
        self.dataset_dir = dataset_dir
        self.out_shape = out_shape
        self.img_size = None

    def __len__(self):
        return len(self.filenames)
    
    def __get_tiles__(self, image, out_shape):
        if self.img_size is None:
          self.img_size = image.size
        return ConstSizeTiles(image_size=image.size, tile_size=out_shape, min_overlapping=0, scale=1.0)
    
    def crop(self):
        
        for index in range(self.__len__()):
            curr_filename = self.filenames[index]
            curr_filename = curr_filename[:-4]
            img_path = os.path.join(self.dataset_dir, self.which_subset, self.which_dataset, 'Images', curr_filename + '.jpg')
            
            if self.which_subset is not 'Test_Dev':
                mask_path = os.path.join(self.dataset_dir, self.which_subset, self.which_dataset, 'Masks', curr_filename + '.png')
                mask = Image.open(mask_path)
                mask_tiles = self.__get_tiles__(mask, self.out_shape)
                
                masks_saving_path = os.path.join(os.path.join(self.dataset_dir, self.which_subset, self.which_dataset, 'cropped_masks'))
                if not os.path.exists(masks_saving_path):
                    os.makedirs(masks_saving_path)
                    
                mask_arr = np.array(mask)
            
                i = 0
                for tile, shape in mask_tiles:
                    img = Image.fromarray(np.uint8(mask_arr[tile[1]:(tile[1]+shape[1]), tile[0]:(tile[0]+shape[0]), :]))
                    img.save(os.path.join(masks_saving_path, curr_filename + "_" + str(i) + ".png"), "PNG")
                    i += 1
                    
            img = Image.open(img_path)
            

            img_tiles = self.__get_tiles__(img, self.out_shape)

            img_arr = np.array(img)
            
            images_saving_path = os.path.join(os.path.join(self.dataset_dir, self.which_subset, self.which_dataset, 'cropped_images'))
            if not os.path.exists(images_saving_path):
                os.makedirs(images_saving_path)

            i = 0
            for tile, shape in img_tiles:
                img = Image.fromarray(np.uint8(img_arr[tile[1]:(tile[1]+shape[1]), tile[0]:(tile[0]+shape[0]), :]))
                img.save(os.path.join(images_saving_path, curr_filename + "_" + str(i) + ".jpg"))
                i += 1
    
    def get_image_size(self):
      return self.img_size
            
            