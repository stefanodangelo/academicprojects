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
        self.images = []
        self.masks = []

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
                    
                mask_arr = np.array(mask)
            
                for tile, shape in mask_tiles:
                    self.masks.append(mask_arr[tile[1]:(tile[1]+shape[1]), tile[0]:(tile[0]+shape[0]), :])
                    
            img = Image.open(img_path)
            

            img_tiles = self.__get_tiles__(img, self.out_shape)

            img_arr = np.array(img)

            for tile, shape in img_tiles:
                self.images.append(img_arr[tile[1]:(tile[1]+shape[1]), tile[0]:(tile[0]+shape[0]), :])
    
    def get_image_size(self):
      return self.img_size

    def get_images(self):
      return self.images

    def get_masks(self):
      if len(self.masks) > 0:
        return self.masks
            
            