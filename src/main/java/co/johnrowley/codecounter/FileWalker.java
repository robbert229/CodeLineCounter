package co.johnrowley.codecounter;

import java.io.File;

public class FileWalker {
	ICommand command;
	
	public FileWalker(ICommand command){
		this.command = command;
	}

	public void walk(String path) {

		File root = new File( path );
		File[] list = root.listFiles();

		if (list == null) return;

		for ( File f : list ) {
      if ( f.isDirectory() ) {
				walk( f.getAbsolutePath() );
				command.directory(f.getAbsolutePath()); //directory
			}
			else {
				command.file(f.getAbsolutePath()); //file
			}
		}
	}
}
