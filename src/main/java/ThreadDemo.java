import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Program: preliminary-understanding-of-the-development-and-use-of-multithread-20190717
 * @Description: 多线程的使用
 * @Author: whx
 * @Create: 2019-07-17 16:15
 **/
public class ThreadDemo {
	
	
	private static UploadService uploadService = new UploadService();
	
	public static void main(String[] args) throws Exception {
		List<File> fileList = getFileList();
		serial(fileList);
		parallel(fileList);
	}
	
	// 模拟串行上传文件
	private static void serial(List<File> fileList) {
		System.out.println("开始串行上传文件 ..");
		int successCount = 0;
		long startTime = Instant.now().toEpochMilli();
		for (File file : fileList) {
			if (uploadService.upload(file)) {
				System.out.println(file.getName() + "上传成功!");
				successCount++;
			}
		}
		long endTime = Instant.now().toEpochMilli();
		System.out.println("串行上传成功个数：" + successCount);
		System.out.println("串行耗时：" + (endTime - startTime) + "ms");
	}
	
	// 模拟并行上传文件
	private static void parallel(List<File> fileList) throws InterruptedException {
		System.out.println("开始并行上传文件 ..");
		CountDownLatch latch = new CountDownLatch(fileList.size());
		AtomicInteger successCount = new AtomicInteger(0);
		long startTime = Instant.now().toEpochMilli();
		for (File file : fileList) {
			new Thread(() -> {
				if (uploadService.upload(file)) {
					System.out.println(file.getName() + "上传成功!");
					successCount.incrementAndGet();
				}
				latch.countDown();
			}).start();
		}
		// 等待所有线程执行结束
		latch.await();
		long endTime = Instant.now().toEpochMilli();
		System.out.println("并行上传成功个数：" + successCount);
		System.out.println("并行耗时：" + (endTime - startTime) + "ms");
	}
	
	private static List<File> getFileList() {
		List<File> fileList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			fileList.add(new File("file" + i));
		}
		return fileList;
	}
	
	static class UploadService {
		
		boolean upload(File file) {
			// 模拟文件上传
			boolean flag = true;
			try {
				TimeUnit.MILLISECONDS.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				flag = false;
			}
			return flag;
		}
		
	}
}

