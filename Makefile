main:
	./gradlew assembleDebug --info --parallel
	adb install -r app/build/outputs/apk/app-debug.apk
