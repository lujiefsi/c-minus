package cminus;

import grammar.TreeNode;

import java.io.FileWriter;
import java.io.IOException;

public class DotWriter {
	FileWriter fileWriter = null;
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
				fileWriter.write(""+root.index);
				fileWriter.write("[label=\""+root+"\"]\n");
				if (root.C0!=null){
					write(root.C0);
					fileWriter.write(""+root.index+"->"+root.C0.index+
							"[label=\"child\"]"+"\n");
				}
				if (root.C1!=null){
					write(root.C1);
					fileWriter.write(""+root.index+"->"+root.C1.index+
							"[label=\"child\"]"+"\n");
				}
				if (root.C2!=null){
					write(root.C2);
					fileWriter.write(""+root.index+"->"+root.C2.index+
							"[label=\"child\"]"+"\n");
				}
				if (root.sibling!=null){
					fileWriter.write(""+root.index+"->"+root.sibling.index+
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
