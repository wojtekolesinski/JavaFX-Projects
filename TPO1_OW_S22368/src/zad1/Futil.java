package zad1;


import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class Futil {

	public static void processDir(String dirName, String resultFileName) {
		try {
			Files.walkFileTree(Paths.get(dirName), new SimpleFileVisitor<Path>(){
				
				private final List<String> lines = new ArrayList<>();

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					Files.deleteIfExists(Paths.get("./" + resultFileName));
					return super.preVisitDirectory(dir, attrs);
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.getFileName().toString().endsWith(".txt")) {
						try (FileChannel inputChannel = FileChannel.open(file)) {
							MappedByteBuffer buffer = inputChannel.map(
									FileChannel.MapMode.READ_ONLY,
									inputChannel.position(),
									inputChannel.size()
							);
							CharBuffer decodedBuffer = Charset.forName("windows-1250").decode(buffer);
							lines.add(new String(decodedBuffer.array()));
						}

					}
					return super.visitFile(file, attrs);
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					try (FileChannel outputChannel = FileChannel.open(Paths.get("./" + resultFileName))) {
						outputChannel.write(StandardCharsets.UTF_8.encode(String.join("\n", lines)));
					}

					return super.postVisitDirectory(dir, exc);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
