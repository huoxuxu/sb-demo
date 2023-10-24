//package com.hxx.sbcommon.common.io.fileOrDir.sliceUpload;
//
//import com.hxx.sbcommon.common.io.fileOrDir.sliceUpload.model.FileUploadDTO;
//import com.hxx.sbcommon.common.io.fileOrDir.sliceUpload.model.FileUploadRequestDTO;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.io.FileUtils;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 分片上传基类
// *
// * @Author: huoxuxu
// * @Description:
// * @Date: 2023-10-21 13:53:23
// **/
//@Slf4j
//public abstract class SliceUploadTemplate {
//
//    public abstract boolean upload(FileUploadRequestDTO param);
//
//    protected File createTmpFile(FileUploadRequestDTO param) {
//        FilePathUtil filePathUtil = SpringContextHolder.getBean(FilePathUtil.class);
//        param.setPath(FileUtil.withoutHeadAndTailDiagonal(param.getPath()));
//        String fileName = param.getFile().getOriginalFilename();
//        String uploadDirPath = filePathUtil.getPath(param);
//        String tempFileName = fileName + "_tmp";
//        File tmpDir = new File(uploadDirPath);
//        File tmpFile = new File(uploadDirPath, tempFileName);
//        if (!tmpDir.exists()) {
//            tmpDir.mkdirs();
//        }
//        return tmpFile;
//    }
//
//    @Override
//    public FileUploadDTO sliceUpload(FileUploadRequestDTO param) {
//
//        boolean isOk = this.upload(param);
//        if (isOk) {
//            File tmpFile = this.createTmpFile(param);
//            FileUploadDTO fileUploadDTO = this.saveAndFileUploadDTO(param.getFile().getOriginalFilename(), tmpFile);
//            return fileUploadDTO;
//        }
//        String md5 = FileMD5Util.getFileMD5(param.getFile());
//
//        Map<Integer, String> map = new HashMap<>();
//        map.put(param.getChunk(), md5);
//        return FileUploadDTO.builder().chunkMd5Info(map).build();
//    }
//
//    /**
//     * 检查并修改文件上传进度
//     */
//    public boolean checkAndSetUploadProgress(FileUploadRequestDTO param, String uploadDirPath) {
//
//        String fileName = param.getFile().getOriginalFilename();
//        File confFile = new File(uploadDirPath, fileName + ".conf");
//        byte isComplete = 0;
//        RandomAccessFile accessConfFile = null;
//        try {
//            accessConfFile = new RandomAccessFile(confFile, "rw");
//            //把该分段标记为 true 表示完成
//            System.out.println("set part " + param.getChunk() + " complete");
//            //创建conf文件文件长度为总分片数，每上传一个分块即向conf文件中写入一个127，那么没上传的位置就是默认0,已上传的就是Byte.MAX_VALUE 127
//            accessConfFile.setLength(param.getChunks());
//            accessConfFile.seek(param.getChunk());
//            accessConfFile.write(Byte.MAX_VALUE);
//
//            //completeList 检查是否全部完成,如果数组里是否全部都是127(全部分片都成功上传)
//            byte[] completeList = FileUtils.readFileToByteArray(confFile);
//            isComplete = Byte.MAX_VALUE;
//            for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
//                //与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
//                isComplete = (byte) (isComplete & completeList[i]);
//                System.out.println("check part " + i + " complete?:" + completeList[i]);
//            }
//
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//        } finally {
//            FileUtil.close(accessConfFile);
//        }
//        boolean isOk = true; // setUploadProgress2Redis(param, uploadDirPath, fileName, confFile, isComplete);
//        return isOk;
//    }
//}
