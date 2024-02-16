package com.sftp;

import java.util.Comparator;
import com.jcraft.jsch.ChannelSftp;


class Sortbyname implements Comparator<ChannelSftp.LsEntry> {

	@Override
	public int compare(ChannelSftp.LsEntry s1, ChannelSftp.LsEntry s2) {
		
		String f1 = s1.getFilename();
		String f2 = s2.getFilename();

        if (isSpecialEntry(f1) || isSpecialEntry(f2)) {
            return 0; 
        }

        int i = getNumericLength(f1);
        int j = getNumericLength(f2);

        int a = Integer.parseInt(f1.substring(0, i));
        int b = Integer.parseInt(f2.substring(0, j));

        return Integer.compare(a, b);
    }

    private static boolean isSpecialEntry(String filename) {
        return filename.equals(".") || filename.equals("..");
    }

    static int getNumericLength(String str) {
        int length = 0;
        while (length < str.length() && Character.isDigit(str.charAt(length))) {
            length++;
        }
        return length;
    }
	
}


