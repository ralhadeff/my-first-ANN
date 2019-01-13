package com.raphael.framework;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FileIO {
    public InputStream readAsset(String fileName) throws IOException;

    public InputStream readFile(String fileName) throws IOException;

    public File writeFile(String fileName) throws IOException;
}
