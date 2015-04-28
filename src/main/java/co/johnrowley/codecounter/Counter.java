package co.johnrowley.codecounter;

class Counter implements ICommand {
  String extension;

  public Counter(String extension){
    this.extension = extension;
  }

  public void file(String path){
		if(path.endsWith("." + extension)){
      CodeFile cf = new CodeFile(path);
		  int lines = cf.lines();
		  System.out.println(path + ": " + lines);
    } 
	}

	public void directory(String path){
		System.out.println("Subdir:" + path);
	}
}
