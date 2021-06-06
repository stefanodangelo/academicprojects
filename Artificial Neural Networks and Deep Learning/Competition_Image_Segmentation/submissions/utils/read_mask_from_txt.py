import numpy as np
from PIL import Image


def read_rgb_mask(img_path, out_shape = None):
    '''
    img_path: path to the mask file
    Returns the numpy array containing target values
    '''

    mask_img = Image.open(img_path)
    if out_shape is not None:
        mask_img = mask_img.resize(out_shape, resample=Image.NEAREST)
    mask_arr = np.array(mask_img)

    new_mask_arr = np.zeros_like(mask_arr)
    
    with open('/content/drive/MyDrive/submissions/utils/RGBtoTarget.txt', 'r') as f:
        lines = f.readlines()
        for line in lines:
            words = line.split()
            rgb_values = words[1:4]
            target = int(words[-2]) 
            if 'background' in line:
                mask_arr_background = [int(rgb_values[0]), int(rgb_values[1]), int(rgb_values[2])]
                target_background = target
            elif 'crop' in line:
                mask_arr_crop = [int(rgb_values[0]), int(rgb_values[1]), int(rgb_values[2])]
                target_crop = target
            elif 'weed' in line:
                mask_arr_weed = [int(rgb_values[0]), int(rgb_values[1]), int(rgb_values[2])]
                target_weed = target
         
    # Use RGB dictionary in 'RGBtoTarget.txt' to convert RGB to target
    new_mask_arr[np.where(np.all(mask_arr == mask_arr_background, axis=-1))] = target_background
    new_mask_arr[np.where(np.all(mask_arr == mask_arr_crop, axis=-1))] = target_crop
    new_mask_arr[np.where(np.all(mask_arr == mask_arr_weed, axis=-1))] = target_weed

    return new_mask_arr