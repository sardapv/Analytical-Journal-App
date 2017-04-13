/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package sample;

import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;

/**
 * The File Download Progress Listener.
 *
 * @author rmistry@google.com (Ravi)
 */
public class FileDownloadProgressListener implements MediaHttpDownloaderProgressListener {

  @Override
  public void progressChanged(MediaHttpDownloader downloader) {
    switch (downloader.getDownloadState()) {
      case MEDIA_IN_PROGRESS:
        //DriveSample.downloadstatus.setText("Download is in progress:");
        View.header1("Download is in progress: " + downloader.getProgress());
        break;
      case MEDIA_COMPLETE:
        //DriveSample.downloadstatus.setText("Download is Complete ");
        View.header1("Download is Complete ");
        break;

    }
  }
}
