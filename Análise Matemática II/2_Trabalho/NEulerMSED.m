%function [t,u,v] = NEulerMSED(f,g,a,b,n,u0,v0)
%NEULERMSED Método de Euler Melhorado para um Sistema de SED/PVI
%   f'=f(t,u,v)
%   g'=g(t,u,v)
%   t=[a,b], u(a)=u0, v(a)=v0;
%   u(i+1) = u(i)+h*f(t(i),u(i),v(i));
%   u(i+1) = u(i)+h2*(f(t(i),u(i),v(i))+f(t(i+1),u(i+1),v(i+1)));
%   v(i+1) = v(i)+h*g(t(i),u(i),v(i));   
%   v(i+1) = v(i)+h2*(g(t(i),u(i),v(i))+g(t(i+1),u(i+1),v(i+1)));     com i=1,2,...,n
%INPUT:
%   f - função da EDO f'=f(t,u,v)
%   g - função da EDO g'=g(t,u,v)
%   [a,b] - intervalo de valores da variável independente t
%   n - núnmero de subintervalos ou iterações do método
%   u0 - aproximação inicial u(a)=u0
%   v0 - aproximação inicial v(a)=v0
%OUTPUT:
%   u - vetor das soluções aproximadas do PVI em cada um dos t(i)
%   v - vetor das soluções aproximadas do PVI em cada um dos t(i)

%Nuno Domingues  a2020109910@isec.pt
%Miguel Neves    a2020146521@isec.pt
%Daniel Albino   a2020134077@isec.pt
%
%Data: 10/05/2021    

function [t,u,v] = NEulerMSED(f,g,a,b,n,u0,v0)

h = (b-a)/n;        % Cálculo do passo
t = a:h:b;          % Alocação de memória
u = zeros(1,n+1);   % Alocação de memória
v = zeros(1,n+1);   % Alocação de memória
u(1) = u0;          % Condição inicial
v(1) = v0;          % Condição inicial
h2 = h/2;           % Atribuição da divisão h/2, para eficiência da função 

    for i = 1:n                                                       %Ciclo com n iterações
        u(i+1) = u(i)+h*f(t(i),u(i),v(i));
        v(i+1) = v(i)+h*g(t(i),u(i),v(i));  
        u(i+1) = u(i)+h2*(f(t(i),u(i),v(i))+f(t(i+1),u(i+1),v(i+1))); % Cálculo do método de Euler Melhorado até n
        v(i+1) = v(i)+h2*(g(t(i),u(i),v(i))+g(t(i+1),u(i+1),v(i+1))); % Cálculo do método de Euler Melhorado até n
    end
end 


