# mitä kaikkea pitäis tehdä

# todo #

  * grafiikat
    * animaatiot, mitä kaikkea tarvitsee
    * kuolinspritet
    * stuude piirtää graffat

  * Powerupit ja erikoisuudet
    * miinat
    * erilaisia zombeja

  * muuta
    * toinen Dpad ja optioni ottaa orientaatio pois?
    * menu (oma activity ja view)
    * koodin refaktorointi


# done #

  * zombit / pelaaja / character luokka
    * damagen ottaminen ja antaminen
    * zombeja syntyy kiihtyvään tahtiin
    * jokin tyyppi muuttuja, jota erilaisia zombeja voi luoda
  * moottorisaha
    * damage (collision detect)
    * rotaten ja sijainnin hiominen
  * zombit
    * arraylist vaihdettu linkedlistiin
    * killedzombies lista poistettu
    * zombi listaa käsitellään synkronoidusti, ettei kaksi threadia muokkaa sitä samaan aikaan
    * random sijainti algoritmi paremmaksi
    * killed zombies listasta eroon
    * pitäisikö zombies arraylist vaihtaa johonkin toiseen

  * pelaaja
    * omaan luokkaan?
    * liikkuminen orientaation mukaan

  * Muuta
    * collision detect, miten tehdään

  * moottorisaha
    * virtual d-pad
    * moottorisahan asento d-padista
    * moottorisaha luokka kuntoon
    * sijainti aina keskelle pelaajaa
