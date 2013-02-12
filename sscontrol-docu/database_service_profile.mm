<map version="0.9.0">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node CREATED="1334562269269" ID="ID_202556306" MODIFIED="1334984003249" STYLE="fork" TEXT="Database Profile">
<font NAME="DejaVu Sans" SIZE="12"/>
<node CREATED="1334562314372" ID="ID_523668949" MODIFIED="1334564795974" POSITION="right" STYLE="fork" TEXT="system">
<font BOLD="true" NAME="DejaVu Sans Mono" SIZE="12"/>
<icon BUILTIN="forward"/>
<node CREATED="1334562317439" ID="ID_1799733363" MODIFIED="1340184641971" STYLE="fork" TEXT="packages_install_command">
<font BOLD="true" NAME="DejaVu Sans Mono" SIZE="12"/>
<icon BUILTIN="forward"/>
<icon BUILTIN="help"/>
<node CREATED="1334984487370" FOLDED="true" ID="ID_34433803" MODIFIED="1340184760715" TEXT="Default">
<node CREATED="1334984490377" ID="ID_183869565" MODIFIED="1334984494421" TEXT="Ubuntu 10.04">
<node CREATED="1334576061203" ID="ID_1218907211" MODIFIED="1334984510843" TEXT="&quot;export DEBIAN_FRONTEND=noninteractive; /usr/bin/aptitude -q -y update &amp;&amp; /usr/bin/aptitude -q -y install&quot;">
<font NAME="DejaVu Sans" SIZE="12"/>
</node>
</node>
</node>
<node CREATED="1334562329929" FOLDED="true" ID="ID_343522910" MODIFIED="1340184759089" STYLE="fork" TEXT="string">
<font BOLD="true" ITALIC="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1334562334428" ID="ID_890187193" MODIFIED="1334564795914" STYLE="fork" TEXT="The command to use to install new packages on the server.">
<edge COLOR="#808080" STYLE="bezier" WIDTH="thin"/>
<font NAME="DejaVu Serif" SIZE="12"/>
</node>
</node>
</node>
<node CREATED="1334562436699" ID="ID_1727919886" MODIFIED="1334564795975" STYLE="fork" TEXT="charset">
<font BOLD="true" NAME="DejaVu Sans Mono" SIZE="12"/>
<icon BUILTIN="forward"/>
<icon BUILTIN="help"/>
<node CREATED="1334562442172" ID="ID_464297129" MODIFIED="1334739244135" STYLE="fork" TEXT="Default &quot;utf-8&quot;">
<edge COLOR="#808080" STYLE="bezier" WIDTH="thin"/>
<font NAME="DejaVu Sans" SIZE="12"/>
</node>
<node CREATED="1334562446836" FOLDED="true" ID="ID_1312688307" MODIFIED="1340184762709" STYLE="fork" TEXT="string">
<font BOLD="true" ITALIC="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1334562449296" ID="ID_1776220947" MODIFIED="1334564795910" STYLE="fork" TEXT="The character set to use for files.">
<edge COLOR="#808080" STYLE="bezier" WIDTH="thin"/>
<font NAME="DejaVu Serif" SIZE="12"/>
</node>
</node>
</node>
</node>
<node CREATED="1334562293513" ID="ID_1005654525" MODIFIED="1334984009209" POSITION="right" STYLE="fork" TEXT="database">
<font BOLD="true" NAME="DejaVu Sans Mono" SIZE="12"/>
<icon BUILTIN="forward"/>
<node CREATED="1334562492357" ID="ID_1478874248" MODIFIED="1334564795968" STYLE="fork" TEXT="handler">
<font BOLD="true" NAME="DejaVu Sans Mono" SIZE="12"/>
<icon BUILTIN="forward"/>
<node CREATED="1334562519986" FOLDED="true" ID="ID_1648121059" MODIFIED="1340184764501" STYLE="fork" TEXT="Ubuntu_10_04_Mysql">
<font BOLD="true" NAME="DejaVu Sans Mono" SIZE="12"/>
<icon BUILTIN="down"/>
<node CREATED="1334562522318" ID="ID_1711005838" MODIFIED="1334984124319" STYLE="fork" TEXT="Uses a MySQL database on a Ubuntu 10.04 server.">
<font NAME="DejaVu Serif" SIZE="12"/>
</node>
</node>
</node>
<node CREATED="1334562560008" ID="ID_1605725955" MODIFIED="1340184644411" STYLE="fork" TEXT="configuration_directory">
<font BOLD="true" NAME="DejaVu Sans Mono" SIZE="12"/>
<icon BUILTIN="forward"/>
<icon BUILTIN="help"/>
<node CREATED="1334984229375" FOLDED="true" ID="ID_749292669" MODIFIED="1340184765343" TEXT="Default">
<node CREATED="1334576144014" ID="ID_89074027" MODIFIED="1334984161481" TEXT="MySQL">
<font NAME="DejaVu Sans" SIZE="12"/>
<node CREATED="1334984162831" ID="ID_702964771" MODIFIED="1334984166311" TEXT="Ubuntu 10.04">
<node CREATED="1334984173907" ID="ID_1030625518" MODIFIED="1334984180742" TEXT="&quot;/etc/mysql/conf.d&quot;"/>
</node>
</node>
</node>
<node CREATED="1334562572279" FOLDED="true" ID="ID_586984228" MODIFIED="1340184765935" STYLE="fork" TEXT="string">
<font BOLD="true" ITALIC="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1334562575149" ID="ID_1882797144" MODIFIED="1334984151128" STYLE="fork" TEXT="The configurations directory for the database server.">
<font NAME="DejaVu Serif" SIZE="12"/>
</node>
</node>
</node>
<node CREATED="1334562709247" ID="ID_1333051368" MODIFIED="1340184644939" STYLE="fork" TEXT="restart_command">
<font BOLD="true" NAME="DejaVu Sans Mono" SIZE="12"/>
<icon BUILTIN="forward"/>
<icon BUILTIN="help"/>
<node CREATED="1334984238266" FOLDED="true" ID="ID_1421211347" MODIFIED="1340184766527" TEXT="Default">
<node CREATED="1334576157808" ID="ID_1576105324" MODIFIED="1334984206229" TEXT="MySQL">
<font NAME="DejaVu Sans" SIZE="12"/>
<node CREATED="1334984206905" ID="ID_875130451" MODIFIED="1334984209952" TEXT="Ubuntu 10.04">
<node CREATED="1334984220421" ID="ID_1510939414" MODIFIED="1334984222108" TEXT="&quot;/sbin/restart mysql&quot;"/>
</node>
</node>
</node>
<node CREATED="1334562710500" FOLDED="true" ID="ID_150303141" MODIFIED="1340184767031" TEXT="string">
<font BOLD="true" ITALIC="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1334562712484" ID="ID_375914160" MODIFIED="1334984201323" STYLE="fork" TEXT="The command that restart the database server.">
<font NAME="DejaVu Serif" SIZE="12"/>
</node>
</node>
</node>
<node CREATED="1334562728394" ID="ID_225498839" MODIFIED="1340184645550" STYLE="fork" TEXT="status_command">
<font BOLD="true" NAME="DejaVu Sans Mono" SIZE="12"/>
<icon BUILTIN="forward"/>
<icon BUILTIN="help"/>
<node CREATED="1334576166140" FOLDED="true" ID="ID_476212701" MODIFIED="1340184767617" TEXT="Default">
<font NAME="DejaVu Sans" SIZE="12"/>
<node CREATED="1334984277048" ID="ID_729200459" MODIFIED="1334984278766" TEXT="MySQL">
<node CREATED="1334984279492" ID="ID_524223210" MODIFIED="1334984282724" TEXT="Ubuntu 10.04">
<node CREATED="1334984289222" ID="ID_845027464" MODIFIED="1334984290488" TEXT="&quot;/sbin/status mysql&quot;"/>
</node>
</node>
</node>
<node CREATED="1334562729551" FOLDED="true" ID="ID_796344992" MODIFIED="1340184769657" TEXT="string">
<font BOLD="true" ITALIC="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1334562731404" ID="ID_476943027" MODIFIED="1334984271763" STYLE="fork" TEXT="The command that checks the status of the database server.">
<font NAME="DejaVu Serif" SIZE="12"/>
</node>
</node>
</node>
<node CREATED="1334714800436" ID="ID_595536727" MODIFIED="1340184646138" TEXT="packages">
<font BOLD="true" NAME="DejaVu Sans Mono" SIZE="12"/>
<icon BUILTIN="forward"/>
<icon BUILTIN="help"/>
<node CREATED="1334714832352" FOLDED="true" ID="ID_1004780912" MODIFIED="1340184768321" TEXT="Default">
<font NAME="DejaVu Sans" SIZE="12"/>
<node CREATED="1334984348304" ID="ID_138001672" MODIFIED="1334984350930" TEXT="MySQL">
<node CREATED="1334984351748" ID="ID_1214019010" MODIFIED="1334984354533" TEXT="Ubuntu 10.04">
<node CREATED="1334984359626" ID="ID_652984473" MODIFIED="1334984360348" TEXT="mysql-server, mysql-client"/>
</node>
</node>
</node>
<node CREATED="1334714808246" FOLDED="true" ID="ID_1932056054" MODIFIED="1340184768863" TEXT="list">
<font BOLD="true" ITALIC="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1334714811154" ID="ID_1845931338" MODIFIED="1334984340656" TEXT="A list of all packages needed to install the database server.">
<font NAME="DejaVu Serif" SIZE="12"/>
</node>
</node>
</node>
<node CREATED="1334562770133" ID="ID_1283616715" MODIFIED="1334984376524" STYLE="fork" TEXT="mysql_admin_command">
<font BOLD="true" NAME="DejaVu Sans Mono" SIZE="12"/>
<icon BUILTIN="forward"/>
<icon BUILTIN="help"/>
<node CREATED="1334562778052" ID="ID_1975306374" MODIFIED="1334984382819" TEXT="Default &quot;mysqladmin&quot;">
<font NAME="DejaVu Sans" SIZE="12"/>
</node>
<node CREATED="1334562771639" FOLDED="true" ID="ID_578197052" MODIFIED="1340184771731" TEXT="string">
<font BOLD="true" ITALIC="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1334562790256" ID="ID_620592528" MODIFIED="1334984400222" STYLE="fork" TEXT="The command for the MySQL administration.">
<font NAME="DejaVu Serif" SIZE="12"/>
</node>
</node>
</node>
<node CREATED="1334562807278" ID="ID_1222019840" MODIFIED="1334564795972" STYLE="fork" TEXT="mysql_command">
<font BOLD="true" NAME="DejaVu Sans Mono" SIZE="12"/>
<icon BUILTIN="forward"/>
<icon BUILTIN="help"/>
<node CREATED="1334562808989" ID="ID_150785641" MODIFIED="1334739204616" TEXT="Default &quot;mysql&quot;">
<font NAME="DejaVu Sans" SIZE="12"/>
</node>
<node CREATED="1334562813512" FOLDED="true" ID="ID_1482982053" MODIFIED="1340184772530" TEXT="string">
<font BOLD="true" ITALIC="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1334562815881" ID="ID_1418379188" MODIFIED="1334714860979" STYLE="fork" TEXT="The command of the MySQL database client.">
<font NAME="DejaVu Serif" SIZE="12"/>
</node>
</node>
</node>
<node CREATED="1334563470019" ID="ID_423273409" MODIFIED="1334984428293" STYLE="fork" TEXT="mysql_status_pattern">
<font BOLD="true" NAME="DejaVu Sans Mono" SIZE="12"/>
<icon BUILTIN="forward"/>
<icon BUILTIN="help"/>
<node CREATED="1334563473896" ID="ID_1046590185" MODIFIED="1334984434219" TEXT="Default &quot;Uptime.*&quot;">
<font NAME="DejaVu Sans" SIZE="12"/>
</node>
<node CREATED="1334563477431" FOLDED="true" ID="ID_55690369" MODIFIED="1340184773389" TEXT="string">
<font BOLD="true" ITALIC="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1334563479685" ID="ID_746016122" MODIFIED="1334984453379" STYLE="fork" TEXT="The expected status from the MySQL database server.">
<font NAME="DejaVu Serif" SIZE="12"/>
</node>
</node>
</node>
</node>
</node>
</map>
