function y = NAdams(f,a,b,n,y0)
%MEULER Método de Adams para resolução numérica de EDO/PVI
%   y'=f(t,y), t=[a,b], y(a)=y0
%   y(i+1)=y(i)+hf(t(i),y(i)), i=0,1,2,...,n
%INPUT:
%   f - função da EDO y'=f(t,y)
%   [a,b] - intervalo de valores da variável independente t
%   n - núnmero de subintervalos ou iterações do método
%   y0 - aproximação inicial y(a)=y0
%OUTPUT:
%   y - vetor das soluções aproximadas do PVI em cada um dos t(i)
%

%Nuno Domingues  a2020109910@isec.pt
%Miguel Neves    a2020146521@isec.pt
%Daniel Albino   a2020134077@isec.pt
%
%Data: 15/04/2021

% SOL. NUMÉRICA DE EQ. DIF. ORDINÁRIAS- Met. Adams Bashforth de ordem 4
% **********************************************************
                                                                                           
h = (b-a)/n;
t = a:h:b;
t(1) = a;
y = zeros(1,n+1);%pré-alocação de memória
y(1) = y0;

% Para iniciar: Runge-Kutta de ordem 4 
b1 = a+3*h;
n1 = 3;

yNAux = NAux(f,a,b1,n1,y0);  %Função auxiliar que calcular os primeiros 4 valores pelo Runge-Kutta de ordem4

y = yNAux;

% Método de Adams - Bashforth de ordem 4(Passo múltiplo)

for i=4:n
    y(i+1)=y(i)+ (h/24) *(55*f(t(i),y(i)) - 59*f(t(i-1),y(i-1)) + 37*f(t(i-2),y(i-2)) - 9*f(t(i-3),y(i-3)));
end
end






   



