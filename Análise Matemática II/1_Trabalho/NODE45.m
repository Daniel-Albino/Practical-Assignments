function y = NODE45(f,a,b,n,y0)
%NODE45 Método ODE45(MatLab) para resolução numérica de EDO/PVI
%   y'=f(t,y), t=[a,b], y(a)=y0
%   [t,y] = ode45(odefun,tspan,y0)
%INPUT:
%   f - função da EDO y'=f(t,y)
%   [a,b] - intervalo de valores da variável independente t
%   n - núnmero de subintervalos ou iterações do método
%   y0 - aproximação inicial y(a)=y0
%OUTPUT:
%   y - vetor das soluções aproximadas do PVI em cada um dos t(i)
%   help ode45

h = (b-a)/n;
t = a:h:b;
[~,y] = ode45(f,t,y0);
y = y.';
end