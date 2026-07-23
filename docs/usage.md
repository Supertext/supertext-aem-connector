# How to use the Supertext connector for AEM 6.5

> Originally published 27 April 2020 by Heinrich Muralt. Rebuilt from the original documentation for this repository.

This is a guide to help order translations from within Adobe Experience Manager (AEM).

![Supertext connector in AEM](assets/featured1.png)

## Steps

### Creating a translation Project

**1. Click on `Create Project` in the projects overview.**

![Create Project](assets/image-18.png)

**2. Select `Translation Project` and click on `Next`.**

![Select Translation Project](assets/image-19.png)

**3. Fill in the mandatory fields of the `Basic` form.** The "due date" may get readjusted in our system if the volume is critical. To change that, contact your Supertext Project Manager.

![Basic form](assets/image-20.png)

**4. Under `Advanced`, select the source and target language(s), `Human Translation` as translation method, `Supertext` as translation provider and the appropriate translation provider credential. Click on `Create`.** (Hint: the selectable languages can be customized — see [the end of this post](#how-to-change-the-languages-in-the-source-and-target-language-dropdowns).)

![Advanced form](assets/image-21.png)

**5. Open the project once successfully created.**

![Open the project](assets/image-23.png)

**6. Click on the three dots of the Translation Job.**

![Translation Job menu](assets/image-24.png)

**7. Add the content that needs to be translated.**

![Add content](assets/image-25.png)

![Content added](assets/image-26.png)

**8. Go back to the project overview by clicking on `Translation Job` and selecting the project name.**

![Back to project overview](assets/image-27.png)

### Requesting the scope

**1. Once the project is created you can get the scope by clicking on `Request Scope`.**

![Request Scope](assets/image-28.png)

**2. Reload the page if the `Status` doesn't switch to `Scope Completed` automatically.**

![Scope Completed](assets/image-29.png)

**3. Click on `Show Scope` to see the scope.**

![Show Scope](assets/image-30.png)

### Starting the translation job

**1. Once the project is created you can start the job by clicking on `Start`.**

![Start the job](assets/image-31.png)

**2. The `Status` changes from `Committed for translation` to `Translation in progress`.** It only shows the updated status after a page reload.

![Translation in progress](assets/image-32.png)

### Reviewing the translation

**1. The Supertext user who ordered the translation will be notified by e-mail when the job is done.** The `Status` of the translation job will then be `Ready for review` (only if `Automatically approve translations` wasn't selected when creating the project!).

![Ready for review](assets/image-33.png)

**2. Click on the three dots of the Translation Job.**

![Translation Job menu](assets/image-34.png)

**3. Select the page you want to review and click on `Preview in Sites`.** It might take a while until the page is completely loaded.

![Preview in Sites](assets/image-35.png)

**4. To accept or reject the translation, select the item and click on `Accept Translation` / `Reject Translation`.**

![Accept or reject translation](assets/image-36.png)

See also: [AEM connector installation guide](installation.md)

## How to change the languages in the source and target language dropdowns

It's possible to customize the language list. This allows you to remove languages from the list or add new ones.

1. Open CRXDE Lite.
2. Create a `core` folder under `/apps/wcm/` and save all (skip this step if the `core` folder already exists).
3. Copy the `resources` folder under `/libs/wcm/core/`.
4. Paste the `resources` folder under `/apps/wcm/core`.
5. Make changes to the language list and save all.
6. Restart AEM, or restart the `TranslationConfigService` from the system console.

> **Note:** The three screenshots that originally illustrated this section could not be recovered — they were no longer available on the original host and were never captured by the Wayback Machine. The written steps above are complete and unchanged.
