### Version 1

Usare N thread per spezzare la lista di Boid, ed ognuno si occupa di un sottogruppo. Devi mettere un mutex su ogni boid per aggiornarlo, perché potrebbe essere che quel boid venga letto da un altro thread?

Con una barrier unica però si rischia che perdano il sync, per essere sicuri servirebbero due barrier diverse.

- Synchronized nel model serve?
- Più reader possono accedere ad un boid, ma il writer deve essere solo. Dividere tra Reader & Writer class?


Listeners JPF:

[https://github.com/javapathfinder/jpf-core/tree/master/src/main/gov/nasa/jpf/listener](https://github.com/javapathfinder/jpf-core/tree/master/src/main/gov/nasa/jpf/listener)
