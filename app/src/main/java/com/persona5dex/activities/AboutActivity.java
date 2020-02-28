package com.persona5dex.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.persona5dex.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by Rechee on 10/9/2017.
 */

public class AboutActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View aboutPage = new AboutPage(this)
                .setDescription(getDescription())
                .addItem(linkElement("Privacy Policy", getString(R.string.privacy_policy_url)))
                .addGroup(getString(R.string.version))
                .addItem(this.getTextElement(this.getVersionNumber()))
                .addGroup(String.format("%s (%s)", getString(R.string.instance_id),
                        getString(R.string.for_debugging)))
                .addGroup(getString(R.string.about_contact_me))
                .addEmail(getString(R.string.email_support))
                .addGroup("Heavily Inspired by")
                .addItem(linkElement("Persona 5 Fusion Calculator", "https://chinhodado.github.io/persona5_calculator/"))
                .addGroup(getString(R.string.about_icon_designer))
                .addItem(linkElement("Nuclear Sky", "https://nuclearskyart.com/"))
                .create();

        setContentView(aboutPage);
    }

    private Element linkElement(String title, String url){
        Element linkElement = new Element();
        linkElement.setTitle(title);
        linkElement.setIconDrawable(R.drawable.about_icon_link);
        linkElement.setIconTint(R.color.personaRed);

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        linkElement.setIntent(i);

        return linkElement;
    }

    private String getDescription() {
        String appName = getString(R.string.app_name);
        String developedBy = getString(R.string.developed_by);

        return String.format("%s\n\n%s:\nRechee Jozil", appName, developedBy);
    }

    private Element versionElement() {
        Element versionElement = new Element();
        versionElement.setTitle(this.getVersionNumber());
        return versionElement;
    }

    private Element getTextElement(String text){
        Element versionElement = new Element();
        versionElement.setTitle(text);
        return versionElement;
    }

    private String getVersionNumber() {
        try {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
