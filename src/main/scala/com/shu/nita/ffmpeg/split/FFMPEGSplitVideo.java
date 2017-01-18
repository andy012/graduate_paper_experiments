package com.shu.nita.ffmpeg.split;

/************************************************************************
 *  The code had been tested but has been changed several times or will	*
 *  be changed to rearrange the code. So there is no warranty that it 	*
 *  will work as it is but the concept works.							*
 ************************************************************************/



import com.shu.nita.ffmpeg.exception.MadsnotepadException;
import com.shu.nita.ffmpeg.util.CmdExecute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class FFMPEGSplitVideo implements SplitVideo {

	public static final String FFMPEG_SPLIT_CMD = "ffmpeg -ss %1$s -i %2$s -t %3$s -acodec copy -vcodec copy -async 1 -y %4$s";
	public static final String FFPROBE_KEYFRAME_CMD = "ffprobe -v quiet -print_format ini -select_streams 0:v:0 -show_entries frame=pkt_pts_time,pict_type %1$s";
	public static final String FFPROBE_DURATION_CMD = "ffprobe -v quiet -print_format ini  -show_entries format=duration %1$s";
    public static final String FFMPEG_GET_INFO = "ffmpeg -i %1$s";
    public static final String FFMEPG_MERGE_FILES= "ffmpeg -f concat -i %1$s -c copy %2$s";
    public static final String SCPFILE= "scp %1$s %2$s";
    //public static final String LIVE555TRANSFORMATION ="/home/havstack/Projects/forAbstract/result/live555MediaServer %1%s ";
    public static final String LIVE555TRANSFORMATION ="/home/havstack/video/live555MediaServer %1%s ";
    public static final String VIDEOMV="mv %1$s %2$s";


	private CmdExecute cmdExecute;
	public CmdExecute getCmdExecute() {
		return this.cmdExecute;
	}

	public void setCmdExecute(CmdExecute cmdExecute) {
		this.cmdExecute = cmdExecute;
	}

    public static void main(String[] args){

        FFMPEGSplitVideo ffmpegSplitVideo=new FFMPEGSplitVideo();
        ffmpegSplitVideo.setCmdExecute(new CmdExecute());

        ffmpegSplitVideo.videoMv("/home/havstack/video/output.mp4","/home/havstack/");
    }



    public void live555VideoFile(String fileLocation){
        try {
            if (getCmdExecute() == null) {
                throw new MadsnotepadException("CmdExecute not set");
            }
            getCmdExecute().execute(String.format(LIVE555TRANSFORMATION,fileLocation));
        } catch (MadsnotepadException e) {
            e.printStackTrace();
        }
    }

    //合并视频
    public void mergeVideoFile(String txtFile, String output){
        try {
            if (getCmdExecute() == null) {
                throw new MadsnotepadException("CmdExecute not set");
            }
            getCmdExecute().execute(String.format(FFMEPG_MERGE_FILES,txtFile,output));
        } catch (MadsnotepadException e) {
            e.printStackTrace();
        }
    }


    public void videoMv(String source, String popurse){
        try {
            if (getCmdExecute() == null) {
                throw new MadsnotepadException("CmdExecute not set");
            }
            getCmdExecute().execute(String.format(SCPFILE,source,popurse));
        } catch (MadsnotepadException e) {
            e.printStackTrace();
        }

    }

    //远程拷贝
    public void VideoScp(String from, String to){
        try {
            if (getCmdExecute() == null) {
                throw new MadsnotepadException("CmdExecute not set");
            }
            getCmdExecute().execute(String.format(SCPFILE,from,to));
        } catch (MadsnotepadException e) {
            e.printStackTrace();
        }

    }

    //获取视频文件的总时间长度
    public int getAllTime(String videoFile) throws IOException {
        try {
        if (getCmdExecute() == null) {
            throw new MadsnotepadException("CmdExecute not set");
        }

            List<InputStream> streams=getCmdExecute().executeNReturnStream(String.format(FFMPEG_GET_INFO, videoFile));
            System.out.println(streams);
            InputStream is = streams.get(1);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while((line=br.readLine())!=null) {
                if(line.contains("Duration")){
                    line=line.substring(line.indexOf(":")+2,line.indexOf("."));
                    int hh= Integer.parseInt(line.substring(0,line.indexOf(":")));
                    int mm= Integer.parseInt(line.substring(line.indexOf(":")+1,line.lastIndexOf(":")));
                    int ss= Integer.parseInt(line.substring(line.lastIndexOf(":")+1,line.length()));

                    System.out.println(line+" "+hh+" "+mm+" "+ss);
                    int sum=hh*3600+mm*60+ss;
                    return sum;
                }
            }

        } catch (MadsnotepadException e) {
            e.printStackTrace();
        }
        return 1;
    }

    //分割视频，安时间进行分割
	public void splitVideo(String videoFile, String outputFile, String startOffset, String endDuration) throws MadsnotepadException {
		if (getCmdExecute() == null) {
			throw new MadsnotepadException("CmdExecute not set");
		}
		getCmdExecute().execute(String.format(FFMPEG_SPLIT_CMD, startOffset, videoFile, endDuration, outputFile));
	}

	private double getVideoDuration(String videoFile) throws MadsnotepadException {
		if (getCmdExecute() == null) {
			throw new MadsnotepadException("CmdExecute not set");
		}
        String line = null;
        double videoDuration = 0.0;
        try {
			List<InputStream> streams = getCmdExecute().executeNReturnStream(String.format(FFPROBE_KEYFRAME_CMD, videoFile));
			if (streams == null || streams.size() != 2) {
				throw new MadsnotepadException("Failed to execute command to retrieve duration");
			}
            InputStream io = streams.get(0);
            BufferedReader br = new BufferedReader(new InputStreamReader(io));
            while ((line = br.readLine()) != null) {
                if (line.contains("duration")) {
                    String durStr = line.substring(line.lastIndexOf("=") + 1, line.length());
                    videoDuration = Double.parseDouble(durStr);
                }
            }
        } catch (Exception e) {
            throw new MadsnotepadException(e);
        }
        return videoDuration;
	}


	private String extractValue(String keyValuePair) {
		return keyValuePair.substring(keyValuePair.lastIndexOf("=") + 1, keyValuePair.length());
	}


	private String getOutputFileName(int counterSuffix, String fileExtn) {
		return String.format("%06d", Integer.parseInt(String.valueOf(counterSuffix))) + fileExtn;
	}

	private String getFileExtn(String fileName) {
		if (fileName == null || fileName.trim().equals("") || !fileName.contains(".")) {
			return null;
		}
		return fileName.substring(fileName.lastIndexOf("."), fileName.length());
	}

	private String getTimeFormat(long seconds) {
        Date date=new Date(seconds);

//		int day = (int) TimeUnit.SECONDS.toDays(seconds);
//        long hour = TimeUnit.SECONDS.toHours(seconds) - (day *24);
//        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
//        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);
        int day = date.getDay();
        long hour = date.getHours();
        long minute =date.getMinutes();
        long second = date.getSeconds();
		return String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second);
	}

	private String getTimeFormat(double seconds) {
		long secInLong = (long)seconds;
		String secStr = Double.toString(seconds);
		if (secStr.indexOf(".") > -1) {
			return getTimeFormat(secInLong) + secStr.substring(secStr.indexOf("."), secStr.length());
		} else {
			return getTimeFormat(secInLong);
		}
	}

    public List<Double> getKeyframes(String videoFile, int nearestToSeconds) throws MadsnotepadException {
        try {
            if (getCmdExecute() == null) {
                throw new MadsnotepadException("CmdExecute not set");
            }
            List<Double> keyFramesList = new ArrayList<Double>();
            List<InputStream> streams = getCmdExecute().executeNReturnStream(String.format(FFPROBE_KEYFRAME_CMD, videoFile));
            if (streams == null || streams.size() != 2) {
                throw new MadsnotepadException("Failed to execute command to retrieve keyframes");
            }
            InputStream is = streams.get(0);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            //breakAt will be the places in multiples of nereastToSeconds. If nereastToSeconds
            //then breakAt will be 5, 10, 15 and so on. It is reset to the next nereastToSeconds
            //after a keyframe is identified near nereastToSeconds
            int breakAt = nearestToSeconds;
            keyFramesList.add(0.0d);
            double dFrame = 0.0d;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (line.contains("pkt_pts_time")) {
                    String frame = extractValue(line);
                    dFrame = Double.parseDouble(frame);
                } else if (line.contains("pict_type")) {
                    String frameType = extractValue(line);
                    if (frameType.equals("I")) {
                        if (dFrame > breakAt) {
                            keyFramesList.add(dFrame);
                            int numberOfKeyFramesIdentified = (int)dFrame/nearestToSeconds;
                            breakAt = nearestToSeconds * (1 + numberOfKeyFramesIdentified);
                        }
                    }
                }
            }
            InputStream err = streams.get(1);
            byte[] b = new byte[1024];
            int size = 0;
            while((size = err.read(b)) != -1){
                System.out.println(new String(b, 0, size));
            }
            return keyFramesList;
        } catch (Exception e) {
            throw new MadsnotepadException(e);
        }
    }

    public List<String> splitVideoAtKeyframes(String videoFile, List<Double> keyFrames) throws MadsnotepadException {
        double totalDurationInSeconds = getVideoDuration(videoFile);
        String outputFile = null;
        String startOffset = null;
        //double splitDuration = 0.0d;
        String splitDuration = null;
        int counter = 0;
        String fileExtn = getFileExtn(videoFile);
        List<String> outputFileList = new ArrayList<String>();
        for (double keyFrame : keyFrames) {
            outputFile = getOutputFileName((counter + 1), fileExtn);
            //check if we have got to the last keyframe and process the content
            //until the end of duration
			/*if (counter >= (keyFrames.size() - 1)) {
				if (keyFrames.get(counter) < totalDurationInSeconds) {
					startOffset = getTimeFormat(keyFrames.get(counter));
					splitDuration = totalDurationInSeconds - keyFrames.get(counter);
					splitVideo(videoFile, outputFile, startOffset, splitDuration);
				}
				break;
			}*/
            startOffset = getTimeFormat(keyFrames.get(counter).intValue());
            splitDuration = getTimeFormat(keyFrames.get(counter+1).intValue());
            System.out.println("startOffset " + startOffset + " splitDuration " + splitDuration);
            splitVideo(videoFile, outputFile, startOffset, splitDuration);
            counter++;
            outputFileList.add(outputFile);
        }
        return outputFileList;
    }


}