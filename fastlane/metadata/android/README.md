# Uploading to Google Play

## Validate first

From the main project directory (not this directory):

```
bundle exec fastlane supply --skip_upload_apk --validate_only --skip_upload_screenshots --skip_upload_images --version_code=10404
```

### Languages in Weblate vs Google Play

You'll notice that the above command will fail with unsupported languages.

As of writing, these are the languages added via Weblate, and their corresponding languages in Google Play that are supported. If we just use the Weblate languages, the upload to Play will fail, so we will rename the directories prior to uploading:

* `cs` -> `cs-CZ`
* `de` -> `de-DE`
* `es` -> `es-ES`
* `fi` -> `fi-FI`
* `fr` -> `fr-FR`
* `it` -> `it-IT`
* `nl` -> `nl-NL`
* `pl` -> `pl-PL`
* `ru` -> `ru-RU`
* `tr` -> `tr-TR`

I've just been doing this in my local working copy prior to running fastlane:

```
mv cs cs-CZ
mv de de-DE
mv es es-ES
mv fi fi-FI
mv fr fr-FR
mv it it-IT
mv nl nl-NL
mv pl pl-PL
mv ru ru-RU
mv tr tr-TR
```

## Actual upload

From the main project directory (not this directory):

```
bundle exec fastlane supply --skip_upload_apk --version_code=10404
```


