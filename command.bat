start "Environment" cmd /c "java -jar environment\target\environment.jar 4444 Environment-1 3000 100"
start "ControlCenter" cmd /c "java -jar control-center\target\control-center.jar 5000 Control"

start "Wisla-1" cmd /c "java -jar river-section\target\river-section.jar 4500 Wisla-1 30 10000"
start "Basin-W-1" cmd /c "java -jar retention-basin\target\retention-basin.jar 4600 Basin-W-1 5000 localhost:5000:Control localhost:4500:Wisla-1"

start "Wisla-2" cmd /c "java -jar river-section\target\river-section.jar 4501 Wisla-2 15 2000 localhost:4600:Basin-W-1"
start "Odra" cmd /c "java -jar river-section\target\river-section.jar 4502 Odra 35 8000"
start "Basin-WO" cmd /c "java -jar retention-basin\target\retention-basin.jar 4601 Basin-WO 5000 localhost:5000:Control localhost:4502:Odra localhost:4501:Wisla-2"

start "Kanal-1" cmd /c "java -jar river-section\target\river-section.jar 4503 Kanal-1 5 2000 localhost:4601:Basin-WO"
start "Kanal-2" cmd /c "java -jar river-section\target\river-section.jar 4504 Kanal-2 5 4000 localhost:4601:Basin-WO"
start "Basin-Merged" cmd /c "java -jar retention-basin\target\retention-basin.jar 4602 Basin-Merged 10000 localhost:5000:Control localhost:4503:Kanal-1 localhost:4504:Kanal-2"