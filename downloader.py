from pytube import YouTube
from tqdm import tqdm
import os
import warnings
import pdb
import ssl
warnings.filterwarnings("ignore")

data_path = './spider/'

name_reference = []
tmp_dict = {}# dict video_name video_url video_id language
video_id = 0
for dir in os.listdir(data_path):
    with open(data_path + dir,'r') as fp:
        lines = fp.readlines()
    for line in tqdm(lines):
        try:
            url = line[:-1]
            yt = YouTube(url)
            filtering = yt.streams.filter(type='audio',subtype='mp4')
            if len(filtering.all())>0:
                name = filtering.first().download('./audio_spider')
                tmp_dict['video_name'] = name
                tmp_dict['video_url'] = url
                tmp_dict['video_id'] = video_id
                tmp_dict['language'] = (dir[0]=='S')
                tmp_dict['filename'] = dir
                name_reference.append(tmp_dict)
                video_id += 1
        except:
            print('Download Error For:', dir, line)
