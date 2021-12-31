package com.example.dditlms.utill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;


public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    String filePath = "C:\\Users\\LMS\\Desktop\\Ddit\\finalProject\\DDITLMS-lecture\\DDITLMS-lecture\\src\\main\\resources\\static\\assignment";

    public List<Map<String,Object>> parseInsertFileInfo(Map<String,Object> map, HttpServletRequest req) throws Exception{
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest)req;

        String original_Name=null;
        String original_Extension=null;
        String stored_Name=null;

        MultipartFile mulFile = null;
        Iterator<String> iterator= mulReq.getFileNames();

        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        Map<String,Object> fileMap = null;

        String board_IDX = (String) map.get("IDX").toString();

        File file = new File(filePath);
        if(file.exists()==false) {
            file.mkdirs();
        }

        while(iterator.hasNext()) {
            mulFile=mulReq.getFile(iterator.next());

            if(mulFile.isEmpty()==false) {
                original_Name=mulFile.getOriginalFilename();
                original_Extension=mulFile.getOriginalFilename().substring(original_Name.lastIndexOf("."));
                stored_Name=CommonUtils.getRandomString()+original_Extension;

                file = new File(filePath+stored_Name);
                mulFile.transferTo(file);

                fileMap = new HashMap<String,Object>();

                fileMap.put("BOARD_IDX", board_IDX);
                fileMap.put("ORIGINAL_FILE_NAME", original_Name);
                fileMap.put("STORED_FILE_NAME", stored_Name);
                fileMap.put("FILE_SIZE", mulFile.getSize());
                fileList.add(fileMap);
            }
        }

        return fileList;
    }
}
