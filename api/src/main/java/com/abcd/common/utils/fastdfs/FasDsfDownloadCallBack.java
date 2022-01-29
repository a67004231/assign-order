package com.abcd.common.utils.fastdfs;

import com.github.tobato.fastdfs.proto.storage.DownloadCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FasDsfDownloadCallBack implements DownloadCallback {

    @Override
    public byte[] recv(InputStream inputStream) throws IOException {
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            byte[] in2b = swapStream.toByteArray();
            return in2b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
