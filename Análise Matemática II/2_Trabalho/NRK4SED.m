function [t,u,v] = NRK4SED(f,g,a,b,n,u0,v0)
%NRK4SED Método de Runge-Kutta de ordem 4 para um Sistema de SED/PVI
%   f'=f(t,u,v)
%   g'=g(t,u,v)
%   t=[a,b], u(a)=u0, v(a)=v0;
%   k1u=h*f(t(i),u(i),v(i));                       
%   k1v=h*g(t(i),u(i),v(i));                       
%   k2u=h*f(t(i)+(h/2),u(i)+(k1u/2),v(i)+(k1v/2));          
%   k2v=h*g(t(i)+(h/2),u(i)+(k1u/2),v(i)+(k1v/2));          
%   k3u=h*f(t(i)+(h/2),u(i)+(k2u/2),v(i)+(k2v/2));          
%   k3v=h*g(t(i)+(h/2),u(i)+(k2u/2),v(i)+(k2v/2));          
%   k4u=h*f(t(i+1),u(i)+k3u,v(i)+k3v);                  
%   k4v=h*g(t(i+1),u(i)+k3u,v(i)+k3v);                  
%   u(i+1) = u(i)+1/6*(k1u+(2*k2u)+(2*k3u)+k4u); 
%   v(i+1) = v(i)+1/6*(k1v+(2*k2v)+(2*k3v)+k4v);       com i=1,2,...,n
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
 
h = (b-a)/n;        % Cálculo do passo
t = a:h:b;          % Alocação de memória
u = zeros(1,n+1);   % Alocação de memória
v = zeros(1,n+1);   % Alocação de memória
u(1) = u0;          % Condição inicial
v(1) = v0;          % Condição inicial

    for i = 1:n                                       %Ciclo com n iterações
        k1u=h*f(t(i),u(i),v(i));                      %Parâmetro K1 da variavel u
        k1v=h*g(t(i),u(i),v(i));                      %Parâmetro K1 da variavel v
        k2u=h*f(t(i)+(h/2),u(i)+(k1u/2),v(i)+(k1v/2));%Parâmetro K2 da variavel u        
        k2v=h*g(t(i)+(h/2),u(i)+(k1u/2),v(i)+(k1v/2));%Parâmetro K2 da variavel v        
        k3u=h*f(t(i)+(h/2),u(i)+(k2u/2),v(i)+(k2v/2));%Parâmetro K3 da variavel u        
        k3v=h*g(t(i)+(h/2),u(i)+(k2u/2),v(i)+(k2v/2));%Parâmetro K3 da variavel v       
        k4u=h*f(t(i+1),u(i)+k3u,v(i)+k3v);            %Parâmetro K4 da variavel u           
        k4v=h*g(t(i+1),u(i)+k3u,v(i)+k3v);            %Parâmetro K4 da variavel v           

        u(i+1) = u(i)+1/6*(k1u+(2*k2u)+(2*k3u)+k4u);  %Cálculo da variável u com o método RK4 até n
        v(i+1) = v(i)+1/6*(k1v+(2*k2v)+(2*k3v)+k4v);  %Cálculo da variável v com o método RK4 até n
    end
end

