wt -w 0 nt --title Environment java -jar environment\target\environment.jar 4444 Environment-1 3000 100
wt -w 0 nt --title ControlCenter java -jar control-center\target\control-center.jar 5000 Control

wt -w 0 nt --title Wisla-1 java -jar river-section\target\river-section.jar 4500 Wisla-1 50 10000
wt -w 0 nt --title Basin-W-1 java -jar retention-basin\target\retention-basin.jar 4600 Basin-W-1 25000 localhost:5000:Control localhost:4500:Wisla-1

wt -w 0 nt --title Wisla-2 java -jar river-section\target\river-section.jar 4501 Wisla-2 60 2000 localhost:4600:Basin-W-1
wt -w 0 nt --title Odra java -jar river-section\target\river-section.jar 4502 Odra 75 8000
wt -w 0 nt --title Basin-WO java -jar retention-basin\target\retention-basin.jar 4601 Basin-WO 50000 localhost:5000:Control localhost:4502:Odra localhost:4501:Wisla-2

wt -w 0 nt --title Kanal-1 java -jar river-section\target\river-section.jar 4503 Kanal-1 5 2000 localhost:4601:Basin-WO
wt -w 0 nt --title Kanal-2 java -jar river-section\target\river-section.jar 4504 Kanal-2 5 4000 localhost:4601:Basin-WO
wt -w 0 nt --title Basin-Merged java -jar retention-basin\target\retention-basin.jar 4602 Basin-Merged 100000 localhost:5000:Control localhost:4503:Kanal-1 localhost:4504:Kanal-2
