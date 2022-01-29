package com.abcd.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @ClassName: GeneratorNo
 * @Description: 序号生成类
 * @author: 邱亮
 * @date: 2018年4月8日 下午2:52:26
 */
public class GeneratorNo {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS");
	private static final DateTimeFormatter FORMATTER_2 = DateTimeFormatter.ofPattern("HHmmssSS");
	private static final AtomicInteger count = new AtomicInteger(0);

	public synchronized static String nextGeneratorOrderNo() {
		LocalDateTime localDateTime = LocalDateTime.now();
		if (count.getAndIncrement() == 10000) {
			count.set(0);
		}
		return localDateTime.format(FORMATTER) + String.format("%04d", count.get());
	}
    public synchronized static String DateStr() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.format(FORMATTER) ;
    }
	public synchronized static String nextGeneratorMemberNo() {
		LocalDateTime localDateTime = LocalDateTime.now();
		if (count.getAndIncrement() == 10000) {
			count.set(0);
		}
		return localDateTime.format(FORMATTER_2) + String.format("%04d", count.get());
	}
}
