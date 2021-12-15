//package com.example.dditlms.utill;
//
//import com.example.dditlms.domain.dto.AtchmnflDTO;
//import com.example.dditlms.service.AtchmnflService;
//import com.sun.xml.internal.ws.api.message.Attachment;
//import org.apache.commons.io.FilenameUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.FileCopyUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Map;
//import java.util.Optional;
//import java.util.UUID;
//
//public class FileUtil {
//    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
//    private final AtchmnflService atchmnflService;
//
//
//    public long uploadFiles(Map<String, MultipartFile> map) {
//        Optional<AtchmnflDTO> atchmnflDTO =
//        Optional<Attachment> attachmentWrapper = attachmentRepository.findFirstByOrderByIdDesc();
//        Attachment topAttachment = attachmentWrapper.orElse(null);
//        long id = 0;
//        if (topAttachment != null) {
//            id = topAttachment.getId() + 1;
//        }
//        int order = 0;
//        for (String key : map.keySet()) {
//            order++;
//            copyAndSave(id, order, map.get(key));
//        }
//        return id;
//    }
//
//    private void copyAndSave(long id, int order, MultipartFile file) {
//        String savedName = FileUtil.uuidMake();
//        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
//        StringBuilder newName = new StringBuilder();
//        newName.append(savedName);
//        newName.append(".");
//        newName.append(extension);
//        File saveFile = new File(PrivateValue.FILEPATH.getValue(), newName.toString());
//        if (!new File(PrivateValue.FILEPATH.getValue()).exists()) {
//            new File(PrivateValue.FILEPATH.getValue()).mkdirs();
//        }
//        try {
//            FileCopyUtils.copy(file.getBytes(), saveFile);
//        } catch (IOException e) {
//        }
//        Attachment attachment = Attachment.builder()
//                .id(id)
//                .order(order)
//                .source(saveFile.getPath())
//                .savedName(savedName)
//                .originName(FilenameUtils.getBaseName(file.getOriginalFilename()))
//                .extension(extension)
//                .size(file.getSize())
//                .downloadCount(0L).build();
//        attachmentRepository.save(attachment);
//    }
//
//    public static String uuidMake() {
//        UUID uuid = UUID.randomUUID();
//        return uuid.toString().replaceAll("-", "");
//    }
//
//}
//
