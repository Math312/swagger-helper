package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public interface SwaggerHelperIcons {

    Icon EXPORT_TEXT_ICON = icon("/icons/swagger.png");

    static Icon icon(String path) {return IconLoader.getIcon(path, SwaggerHelperIcons.class);}
}
