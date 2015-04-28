package co.johnrowley.codecounter;

interface ICommand {
	public void file(String path);
	public void directory(String path);
}
