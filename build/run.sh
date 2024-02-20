#!/bin/bash
ip=$(hostname -I)
screen -S "admin-panel" -d -m
screen -S "telegram-bot" -d -m
screen -r "admin-panel" -X stuff $"java -jar /home/bot/admin-panel.jar^M" &
screen -r "telegram-bot" -X stuff $"java -jar -Dadmin-panel.url=https://${ip// /}:8443/admin /home/bot/telegram-bot-jar-with-dependencies.jar^M"
