package com.shu.nita.ffmpeg.split;

/************************************************************************
 *  The code had been tested but has been changed several times or will *
 *  be changed to rearrange the code. So there is no warranty that it 	*
 *  will work as it is but the concept works.							*
 ************************************************************************/


import com.shu.nita.ffmpeg.exception.MadsnotepadException;

import java.util.List;


public interface SplitVideo {

	public List<Double> getKeyframes(String videoFile, int nearestToSeconds) throws MadsnotepadException;

	public List<String> splitVideoAtKeyframes(String sourceVideo, List<Double> keyframeList) throws MadsnotepadException;

	public void splitVideo(String sourceVideo, String outputFile, String startOffset, String endDuration) throws MadsnotepadException;

}