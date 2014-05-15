/**
 *
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package com.github.born2snipe.maven.plugin.idea.sandbox;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;

@Mojo(name = "delete-plugin-from-sandbox", requiresProject = true)
public class DeletePluginFromSandboxMojo extends AbstractMojo {
    @Parameter(required = true, readonly = true, property = "project")
    private MavenProject project;
    @Parameter(defaultValue = "${user.home}/Library/Caches/IntelliJIdea13")
    private File ideaCacheDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            File directory = projectSandboxDirectory();
            if (directory.exists()) {
                getLog().info("Deleting plugin from sandbox: " + directory);
                FileUtils.deleteDirectory(directory);
            } else {
                getLog().info("Nothing to delete from the sandbox");
            }
        } catch (IOException e) {
            throw new MojoExecutionException("A problem occurred when attempting to delete the old sandboxed plugin", e);
        }
    }

    private File projectSandboxDirectory() {
        return new File(pluginsSandboxDirectory(), project.getName());
    }

    private File pluginsSandboxDirectory() {
        return new File(ideaCacheDirectory, "plugins-sandbox/plugins");
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public void setIdeaCacheDirectory(File ideaCacheDirectory) {
        this.ideaCacheDirectory = ideaCacheDirectory;
    }
}
