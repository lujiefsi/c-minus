package cminus;

import grammar.TreeNode;

import java.io.FileWriter;
import java.io.IOException;

public class DotWriter {
	FileWriter fileWriter = null;
	private int index = 0;
	public DotWriter(String file) {
		try {
			fileWriter = new FileWriter(file);
			fileWriter.write("digraph AST{\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(TreeNode root) {
		while (root != null) {
			try {
				fileWriter.write(""+root.localIndex);
				fileWriter.write("[label=\""+root+"\"]\n");
				if (root.C0!=null){
					write(root.C0);
					fileWriter.write(""+root.localIndex+"->"+root.C0.localIndex+
							"[label=\"child\"]"+"\n");
				}
				if (root.C1!=null){
					write(root.C1);
					fileWriter.write(""+root.localIndex+"->"+root.C1.localIndex+
							"[label=\"child\"]"+"\n");
				}
				if (root.C2!=null){
					write(root.C2);
					fileWriter.write(""+root.localIndex+"->"+root.C2.localIndex+
							"[label=\"child\"]"+"\n");
				}
				if (root.sibling!=null){
					fileWriter.write(""+root.localIndex+"->"+root.sibling.localIndex+
							"[label=\"sibling\"]"+"\n");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			root = root.sibling;
		}
	}

	public void close() {
		if (fileWriter != null) {
			try {
				fileWriter.write("}\n");
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
