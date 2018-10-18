1 - O aplicativo inicia mostrando uma lista, um RecycledView object preenchido com a imagem da fruta, o nome da fruta e o pre�o em dolar
2 - Ao clicar no item em quest�o, � mostrado o detalhe com a imagem da fruta em plano maior, seu nome em destaque e o pre�o em dolar e o
    pre�o em real convertido por uma chamada nativa em C.
    
Decis�es tomadas:
1 - Para fazer o cache de imagens optou-se por armazen�-las em um banco SQLite, ver detalhes na classe FrutasDBHelper
2 - Optou-se utilizar a arquitetura (MVP - Model, View , Presenter) em que para fazer a inteface com o model foi criado um
contrato (ver classe FrutasContract)

View - em nosso caso � o MainFragment.java e FrutaDetalheFragment que mostram respectivamente a lista e o detalhe dos dados, 
n�o cont�m regra alguma do neg�cio a n�o ser disparar eventos que notificam mudan�a de estado dos dados que ele exibe e 
processamento pr�prio dele. O RecycledView utilizado implementa uma interface que exp�e campos e eventos que o presenter necessita.

Model - S�o os objetos que ser�o manipulados. Um objeto Model implementa uma interface que exp�e os campos que o presenter 
ir� atualizar quando sofrerem altera��o na view. Ver classe ItemFruta.java

Presenter - � a liga��o entre View e Model, possui papel de mediador entre eles. Ele � encarregado de atualizar o view
quando o model � alterado e de sincronizar o model em rela��o ao view. Em nosso caso � o FrutasSyncAdapter.java e o 
CalculeNative.java que chama o programa em C (cambio-lib.cpp) que faz a convers�o de dolar para real.