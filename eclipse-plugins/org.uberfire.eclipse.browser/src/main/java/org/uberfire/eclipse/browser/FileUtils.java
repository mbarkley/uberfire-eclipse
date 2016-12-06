package org.uberfire.eclipse.browser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.compiler.builder.impl.KnowledgeBuilderConfigurationImpl;
import org.drools.compiler.builder.impl.KnowledgeBuilderImpl;
import org.drools.compiler.compiler.BaseKnowledgeBuilderResultImpl;
import org.drools.compiler.compiler.DrlParser;
import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.compiler.PackageRegistry;
import org.drools.compiler.lang.descr.PackageDescr;
import org.drools.compiler.rule.builder.dialect.java.JavaDialectConfiguration;
import org.drools.core.definitions.impl.KnowledgePackageImpl;
import org.drools.core.util.StringUtils;
import org.drools.eclipse.DRLInfo;
import org.drools.eclipse.DRLInfo.FunctionInfo;
import org.drools.eclipse.DRLInfo.RuleInfo;
import org.drools.eclipse.builder.DroolsBuilder;
import org.drools.eclipse.builder.Util;
import org.drools.eclipse.dsl.editor.DSLAdapter;
import org.drools.eclipse.util.ProjectClassLoader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.uberfire.backend.vfs.PathFactory;

public class FileUtils {

	public static IFile getFile(String uriString) {
		IFile file = null;
		if (uriString != null) {
			try {
				URI uri = new URI(uriString);
				IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
				IPath location = Path.fromOSString(uri.getPath());
				file = workspaceRoot.getFileForLocation(location);
				if (file == null || !file.exists())
					file = null;
			} catch (Exception e) {
			}
		}
		return file;
	}

	public static String read(IFile file) throws CoreException {
		InputStream inputStream = file.getContents();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder sb = new StringBuilder();
		try {
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				sb.append(buf, 0, numRead);
			}
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				// Nothing
			}
		}
		return sb.toString();
	}

	public static int write(IFile file, String contents) {
		InputStream inputStream = new ByteArrayInputStream(contents.getBytes());
		try {
			file.setContents(inputStream, true, true, null);
		} catch (Exception e) {
			return -1;
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return contents.length();
	}

	public static org.uberfire.backend.vfs.Path createVfsPath(String uri) {
		String filename = uri;
		int i = uri.lastIndexOf("/");
		if (i > 0)
			filename = uri.substring(i + 1);
		org.uberfire.backend.vfs.Path path = PathFactory.newPath(filename, uri);
		return path;
	}
}
