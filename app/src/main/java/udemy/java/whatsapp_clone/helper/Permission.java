package udemy.java.whatsapp_clone.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permission {

    public static void permissionValidation(String[] permissions, Activity activity, int requestCode){

        if (Build.VERSION.SDK_INT >= 23){
            List<String> listPermissions = new ArrayList<>();

            /* Vai ler as permissões passadas
            e verificar uma a uma
            * se a permissao ja foi  libertada
            **/

            for (String permission : permissions) {
               Boolean wasPermission = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
                if (!wasPermission){
                    listPermissions.add(permission);
                }
            }

            if (listPermissions.isEmpty()){
                return;
            }

            String[] newPermissions = new String[listPermissions.size()];
            listPermissions.toArray(newPermissions);

            ActivityCompat.requestPermissions(activity, newPermissions, requestCode);
        }

    };

}
