package ec.wraper.ffmpeg;

public class EncodeSeqImagesToVideo extends FFmpegCommand {




    private int fps = 30;
    private FFmpegHelper.Resolution resolution = FFmpegHelper.Resolution.P1080;

    //Input Files Pattern Sample : %03d.png 0001.png , 0002.png
    private String inputFilesPattern = null;


    private String codec = "libx264";
    private int quality = 25;
    private String pixelFmt = "yuv420p";
    private String outToFile = null;

    public EncodeSeqImagesToVideo(ProcessInvoker invoker) {
        super(invoker);
    }

    @Override
    protected void validate() throws Exception {
        if(quality < 15 || quality > 25) throw new Exception("Quality must be 15 to 25");
        if(inputFilesPattern == null) throw new Exception("Input File pattern can't be null");
        if(outToFile == null) throw new Exception("Output File can't be null");
        if(FFmpegHelper.toResolution(resolution) == null) throw new Exception("Resolution can't be null");
    }

    @Override
    public String command() {
        StringBuffer buff = new StringBuffer();
        buff.append("ffmpeg -r " + fps);
        buff.append(" -s " + FFmpegHelper.toResolution(resolution));
        buff.append(" -i " + inputFilesPattern);
        buff.append(" -vcodec " + codec);
        buff.append(" -crf " + quality);
        buff.append("  -pix_fmt " + pixelFmt);
        buff.append(" " + outToFile);
        return buff.toString();
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void setResolution(FFmpegHelper.Resolution resolution) {
        this.resolution = resolution;
    }

    public void setInputFilesPattern(String inputFilesPattern) {
        this.inputFilesPattern = inputFilesPattern;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public void setPixelFmt(String pixelFmt) {
        this.pixelFmt = pixelFmt;
    }

    public void setOutToFile(String outToFile) {
        this.outToFile = outToFile;
    }


    @Override
    public String toString() {
        return "EncodeSeqImagesToVideo{" +
                "fps=" + fps +
                ", resolution=" + resolution +
                ", inputFilesPattern='" + inputFilesPattern + '\'' +
                ", codec='" + codec + '\'' +
                ", quality=" + quality +
                ", pixelFmt='" + pixelFmt + '\'' +
                ", outToFile='" + outToFile + '\'' +
                '}';
    }
}
