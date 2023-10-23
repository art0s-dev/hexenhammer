# Hexenhammer 40k


## Allgemeines
Das ist eine Kleine Anwendung zum "Mathhammern" von verschiedenen Einheiten
Das Ziel dieser Anwendung ist es eine kleine selbständige GUI Anwendung zu entwickeln,
mit der man am Desktop die Effektivität seiner Einheiten berechnen kann.

## Tech Stack
Ich möchte zur Erstellung des ganzen folgendes Verwenden:
- Sqlite für eine Lokale Datenbank
- Hibernate, da die Entitys hier im Mittelpunkt stehen und
wiederverwendet werden sollen
- SWT als Gui, da ich auf linux, macos und windows das binary platzieren will
- Ich möchte "nur" eine exe verschiffen mit der version also will ich graalvm native images benutzen
- Zum Testen will ich Junit 5 verwenden 
- Zum Laufen der Integrationstests will ich verschiedene präparierte sqlite Datenbanken verwenden.
- Der Fokus soll im Tech stack allerdings auf dem GUI und der Statistischen Auswertung liegen.

## Idee
Ich möchte an dieser Stelle Datenzentriert arbeiten. Also wird sich alles um die Entitys drehen.
An dieser Stelle Unit genannt. Jede Unit hat unterschiedliche Profilwerte, welche vom Nutzer 
eingegeben werden können. Der Datenstand soll gespeichert werden können und geladen werden können.
Also der Nutzer soll sich die sqlite Datenbanken selber ziehen und mit seinen Freunden austauschen können.
Also es soll eine Import und Export Funktion der Datenbank geben. Dem Nutzer werden dann anhand der Datenlage
unterschiedliche Tools zum Analysieren und herumspielen mit den Units gegeben.
Das hat zur Folge, dass der einzig wichtige State hierbei die austauschbare Datenbank ist.
Das heißt, wenn ich die Datenbank tausche, sollten sich die Ergebnisse, Einstellungen etc auch ändern.

Die Anwendung soll nach Abschluss "feature complete" sein, also danach wird nichts mehr hinzugefügt, sondern
lediglich noch bugfixes gemacht oder vllt man ne major version alle jubeljahre, da ich keine
virtualsierung und keine cicd verwenden möchte. Die Leute sollen sich das ganze einfach über mein Github repo ziehen.

Mir ist es wichtig, dass keine Breaking Changes gemacht werden. Also dass nachdem das Projekt veröffentlicht wurde keine 
weiteren Änderungen an den Entitys vorgenommen werden ( also keine Migrationen! ), da Migrationsmanagement
einfach den Hobbyaufwand übersteigt -- und ich da auch ein bisschen zu Faul für bin ;)

# Architektur 
Ich möchte eine Simple Architektur, welche es mir ermöglich einen ganz niedrigen 
Wartungsaufwand zu haben ( Grugbrained Dev ). Alle Interaktionen in diesem System,
werden durch ein Interface beschrieben und anschließend nach Unten Implementiert.
Der Ordner wird `usecases` genannt und jedes Feature wird darin als Klasse beschrieben.

> Damit das ganze einfach zu verstehen ist, haben wir z.B das Feature Import
> Und alle Aktionen mit dem Import sollen da aufgeführt werden
> Sodass der Nutzer mit dem GUI hinterher diese Schnittstellen ohne Umwege direkt nutzen kann

Das mache ich nicht weil ich glaube das Später austauschen zu müssen, 
sondern weils einfach Sauberer is. Also eine richtig abgespeckte variante 
der Hexagonalen Architektur.

Aber jede Architektur hat einen Core. Dieser Core ist bei mir wie gesagt die Unit. Das wird eine Hibernate entity sein,
welche mit den verschiedenen Profilwerten ausgerüstet wird. Ich weiß, dass 40k Verschiedene Spezialeffekt hat
und dass ich diesem "Wust" an sonderregeln irgendwie in meiner Anwendung abbilden muss.
Aber das ist ein Problem für Zukunfts-Till. Deshalb die Flexible Architektur.

## Features

Das Core Feature soll das Laden und Vergleichen von units sein
mit der ergänzung des "Teilens" von Datenständen mit freunden.
Also quasi ein Collaborativer Calculator.
Also Wichtig ist:
- Unit Cruch mit Profilwerden (Own Unit und Target unit)
- Liste vom Profilen gegen mehrere Profile auswerten und
Darstellen lassen ( also gegen guardsmen, space marines, leman russ, knight etc )
- Auswertung von Damage / Punktekosten und tabellarische Darstellung
- Import und Export von verschiedenen Datenständen

