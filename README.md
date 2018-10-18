1 - O aplicativo inicia mostrando uma lista, um RecycledView object preenchido com a imagem da fruta, o nome da fruta e o preço em dolar
2 - Ao clicar no item em questão, é mostrado o detalhe com a imagem da fruta em plano maior, seu nome em destaque e o preço em dolar e o
    preço em real convertido por uma chamada nativa em C.
    
Decisões tomadas:
1 - Para fazer o cache de imagens optou-se por armazená-las em um banco SQLite, ver detalhes na classe FrutasDBHelper
2 - Optou-se utilizar a arquitetura (MVP - Model, View , Presenter) em que para fazer a inteface com o model foi criado um
contrato (ver classe FrutasContract)

View - em nosso caso é o MainFragment.java e FrutaDetalheFragment que mostram respectivamente a lista e o detalhe dos dados, 
não contém regra alguma do negócio a não ser disparar eventos que notificam mudança de estado dos dados que ele exibe e 
processamento próprio dele. O RecycledView utilizado implementa uma interface que expõe campos e eventos que o presenter necessita.

Model - São os objetos que serão manipulados. Um objeto Model implementa uma interface que expõe os campos que o presenter 
irá atualizar quando sofrerem alteração na view. Ver classe ItemFruta.java

Presenter - É a ligação entre View e Model, possui papel de mediador entre eles. Ele é encarregado de atualizar o view
quando o model é alterado e de sincronizar o model em relação ao view. Em nosso caso é o FrutasSyncAdapter.java e o 
CalculeNative.java que chama o programa em C (cambio-lib.cpp) que faz a conversão de dolar para real.