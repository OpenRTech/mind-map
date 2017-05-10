function Display(canvas, draw)
{
	var lastOfset = {'x':0, 'y':0};
	var translated = {'x':0, 'y':0};
	var isDragging = false; // флаг говорящий тащим ли мы сейчас канву или нет 
	var ctx = canvas.getContext('2d');

///////////////////////// получаем координаты мышки на карте //////////////////////////////////////////
	this.getPositionOnMap = function(x, y){return {'x':x-translated.x, 'y':y-translated.y};};

///////////////////////////////////////////////////////////////////////////////////////////////////////
	var _clear =function() 
	{
		ctx.fillStyle="#ffffff";
		ctx.fillRect(-translated.x,-translated.y,canvas.width,canvas.height);
		ctx.fillStyle="#888888";
		ctx.strokeRect(-translated.x,-translated.y,canvas.width,canvas.height);
	}
/////////////////////// перемещение канвы вслед за мышкой ////////////////////////////////////////////

	var startChangeOfset = function (e) 
	{
		var evt = e || event;
		isDragging = true;
		lastOfset.x = evt.offsetX;
		lastOfset.y = evt.offsetY;
	};

	var changeOfset = function (e)
	{
		var evt = e || event;
		if (isDragging) 
		{
			var deltaX = evt.offsetX-lastOfset.x;
			var deltaY = evt.offsetY-lastOfset.y;
			translated.x += deltaX;
			translated.y += deltaY;
			ctx.translate(deltaX, deltaY);
			lastOfset.x = evt.offsetX;
			lastOfset.y = evt.offsetY;
			_clear();
			draw();
		}
	};

	stopChangeOfset = function (e)
	{
		isDragging = false;
	};
////////////////////////////////////////////////////////////////////////////////////////////////////


	canvas.onmousedown = startChangeOfset;
	canvas.onmousemove = changeOfset;
	canvas.onmouseup = stopChangeOfset;

};
