// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: Apache-2.0

package software.aws.toolkits.jetbrains.services.lambda.upload;

import static software.aws.toolkits.resources.Localization.message;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.util.textCompletion.TextFieldWithCompletion;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import org.jetbrains.annotations.NotNull;
import software.amazon.awssdk.services.lambda.model.Runtime;
import software.aws.toolkits.jetbrains.services.iam.IamRole;
import software.aws.toolkits.jetbrains.services.lambda.HandlerCompletionProvider;
import software.aws.toolkits.jetbrains.ui.EnvironmentVariablesTextField;

@SuppressWarnings("NullableProblems")
public class EditFunctionPanel {
    @NotNull JTextField name;
    @NotNull JTextField description;
    @NotNull EditorTextField handler;
    @NotNull JButton createRole;
    @NotNull JButton createBucket;
    @NotNull JPanel content;
    @NotNull ComboBox<IamRole> iamRole;
    @NotNull ComboBox<Runtime> runtime;
    @NotNull ComboBox<String> sourceBucket;
    @NotNull EnvironmentVariablesTextField envVars;
    @NotNull JTextField timeout;
    @NotNull JPanel deploySettings;
    @NotNull JTextField memorySize;
    @NotNull JSlider memorySlider;
    @NotNull JSlider timeoutSlider;
    @NotNull JPanel configurationSettings;
    @NotNull JLabel handlerLabel;

    private final Project project;

    EditFunctionPanel(Project project) {
        this.project = project;

        deploySettings.setBorder(IdeBorderFactory.createTitledBorder(message("lambda.upload.deployment_settings"), false));
        configurationSettings.setBorder(IdeBorderFactory.createTitledBorder(message("lambda.upload.configuration_settings"), false));
    }

    private void createUIComponents() {
        handler = new TextFieldWithCompletion(project, new HandlerCompletionProvider(project), "", true, true, true, true);
    }
}
