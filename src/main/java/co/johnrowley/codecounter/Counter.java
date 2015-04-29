package co.johnrowley.codecounter;

import java.util.*;

class Counter implements ICommand {
  String extension;
  ArrayList<CodeFile> codeFiles;

  public Counter(String extension){
    this.extension = extension;
    codeFiles = new ArrayList<CodeFile>();
  }
  
  public ArrayList<CodeFile> getCodeFiles(){
      return codeFiles;
  }

  public void file(String path){
		if(path.endsWith("." + extension)){
      codeFiles.add(new CodeFile(path));
    } 
	}

	public void directory(String path){
		System.out.println("Subdir:" + path);
	}
}
