1 - O aplicativo inicia mostrando uma lista, um RecycledView object 
    preenchido com a imagem da fruta, o nome da fruta e o pre�o em dolar
2 - Ao clicar no item em quest�o, � mostrado o detalhe com a imagem da 
    fruta em plano maior, seu nome em destaque e o pre�o em dolar e o
    pre�o em real convertido por uma chamada nativa em C.
    
Decis�es tomadas:
1 - Para fazer o cache de imagens optou-se por persist�-las no 
    SQLite, ver detalhes na classe FrutasDBHelper
2 - Optou-se utilizar a arquitetura (MVP - Model, View , Presenter) 
    em que para fazer a inteface com o model foi criado um
    contrato (ver classe FrutasContract)
    
View - em nosso caso � o MainFragment.java e FrutaDetalheFragment que mostram 
respectivamente a lista e o detalhe dos dados, n�o cont�m regra alguma do neg�cio 
a n�o ser disparar eventos que notificam mudan�a de estado dos dados que ele exibe e 
processamento pr�prio dele. O RecycledView utilizado implementa uma interface que 
exp�e campos e eventos que o presenter necessita.

Model - S�o os objetos que ser�o manipulados. Um objeto Model implementa uma interface 
que exp�e os campos que o presenter ir� atualizar quando sofrerem altera��o na view. 
Ver classe ItemFruta.java

Presenter - � a liga��o entre View e Model, possui papel de mediador entre eles. Ele � 
encarregado de atualizar o view quando o model � alterado e de sincronizar o model em 
rela��o ao view. Em nosso caso � o MainActivity.java que congrega o acesso por meio da 
interface APIServiceGET.java que faz o elo com o webservice e CalculeNative.java que � 
o elo com o programa em C (cambio-lib.cpp) que faz a convers�o de dolar para real.

3 - Foi utilizado o Retrofit como framework de comunica��o com a API
4 - Optou-se por n�o ser utilizado o butterknife, pois o uso do findViewById 
    nas vers�es mais recentes do Android n�o exige mais o uso expli�cito do cast, 
    al�m disso o uso � plenamente dispens�vel caso seja utilizado o Kotlin e 
    facilita sua migra��o para esta linguagem.
5 - Optou-se utilizar o CMakeList.txt em vez do Android.mk, pois o App foi constru�do 
    no Android Studio vers�o 3.2.1 em que � requerida uma menor complexidade na montagem 
    do ambiente e sobretudo pela indisponibilidade de um ambiente confi�vel a ser
    montado no Windows 10 para uso do GNU Tools. Se tivesse um ambiente Linux poderia 
    ser feito com mais praticidade o uso do Android.mk e ndk-build em vez do CMake.

