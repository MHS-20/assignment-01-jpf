### Version 1

Usare N thread per spezzare la lista di Boid, ed ognuno si occupa di un sottogruppo. Devi mettere un lock su ogni boid per aggiornarlo, perché potrebbe essere che quel boid venga letto da un altro thread.

Synchronized nel model serve? Per i campi che possono cambiare si.

Velocity/Position: necessarie due barriere su cui aspettano tutti.

RWLock per accedere ai campi del boid funziona meglio di usare due barriere in più. Rendere ogni Boid un monitor lo rallenta troppo perché serializza anche le letture. Ogni boid ha il suo RWLock, così le letture sono parallele.

Listeners JPF:

[https://github.com/javapathfinder/jpf-core/tree/master/src/main/gov/nasa/jpf/listener](https://github.com/javapathfinder/jpf-core/tree/master/src/main/gov/nasa/jpf/listener)
