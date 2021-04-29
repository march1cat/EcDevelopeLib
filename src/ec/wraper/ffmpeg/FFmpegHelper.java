package ec.wraper.ffmpeg;

import ec.system.OsCommandExecuter;

public class FFmpegHelper {


    public enum Resolution {
        P1080 , P720
    };


    public static FFmpegCommand encodeSequenceImagesToMp4(
            ProcessInvoker invoker , int fps ,
            FFmpegHelper.Resolution resolution ,
            String inputFilesPattern ,
            int quality ,
            String outFile
            ) throws Exception {
        EncodeSeqImagesToVideo command = new EncodeSeqImagesToVideo(invoker);
        command.setFps(fps);
        command.setResolution(resolution);
        command.setInputFilesPattern(inputFilesPattern);
        command.setQuality(quality);
        command.setOutToFile(outFile);
        command.validate();
        bindOsExecuter(command);
        return command;
    }


    private static void bindOsExecuter(FFmpegCommand command){
        OsCommandExecuter executer = new OsCommandExecuter();
        command.setExecuter(executer);
    }

    protected static String toResolution(Resolution r){
        if(r == Resolution.P1080) return "1920x1080";
        if(r == Resolution.P720) return "1280x720";
        return null;
    }


}
