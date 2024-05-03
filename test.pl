%atacar(Dist) :- Dist < 1000.
%acelerar(Dist, Velocidade) :- Dist >= 500, Velocidade < 5.0.
%fugir(Energia) :- Energia < 40.0.
velocidade(Aceleracao*Tempo) :- Aceleracao >= 1, Tempo >= 0.1. 
direcao(Velocidade) :- Velocidade > 0 ; Velocidade < 0;




% Calcula a velocidade proporcional ao erro
calcular_velocidade(Alvo, Velocidade) :-
    calcular_erro(Alvo, Erro),
    constante_proporcional(Kp),
    Velocidade is Kp * Erro.

% Calcula o erro (diferença entre a posição atual e a posição desejada do alvo)
calcular_erro(Alvo, Erro) :-
    posição_atual(Robô, PosiçãoAtual),
    posição_desejada(Alvo, PosiçãoDesejada),
    calcular_distância(PosiçãoAtual, PosiçãoDesejada, Erro).

% Predicados fictícios para a posição atual do robô e a posição desejada do alvo
posição_atual(robô1, [X1, Y1]).
posição_desejada(alvo1, [X2, Y2]).

% Predicado fictício para calcular a distância entre duas posições
calcular_distância([X1, Y1], [X2, Y2], Dist) :-
    Dist is sqrt((X2 - X1)^2 + (Y2 - Y1)^2).

