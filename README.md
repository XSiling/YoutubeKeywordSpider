# YoutubeKeywordSpider

## How to use

### 1. Prepare Cookies

Download plug-in for cookies from https://chrome.google.com/webstore/detail/editthiscookie/fngmhnnpilhplaeedifhccceomclgfbg?hl=zh. Log in YouTube then save the Cookies using this. The file name should be 'youtubecookie.json'

### 2. Edit Keywords then Run

Run YouTubeKeywordSpider.java in any IDEA. The environment is java 1.8. Currently, the code only support search for one keyword per time. If u want to watch videos from certain Area, u need to change ur ip manually. The urls of relevant videos should be saved in '\<keyword\>.txt'.

### 3. Download Video/Audio

Pytube is used in downloading. Run downloader.py and check the parameters of location in file. In downloader, only audio will be downloaded.
