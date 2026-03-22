package games.generic.view.dataProviders;

import games.generic.controlModel.holders.EquipmentsHolder;
import games.generic.controlModel.items.EquipmentSet;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.view.GameView;
import games.generic.view.impl.GuiComponent;

/** Mostra quali pezzi di equipaggiamento sono assegnati al Player. */
public class EquipSetProviderGui extends GuiComponent {

	public EquipSetProviderGui(GameView view) { super(view); }

	public EquipmentSet getEquipSet() {
		PlayerGeneric p;
		EquipmentsHolder eh;
		p = view.getCurrentPlayerInGame();
		if (!(p instanceof EquipmentsHolder))
			return null;
		eh = (EquipmentsHolder) p;
		return eh.getEquipmentSet();
	}

	@Override
	public void onAddingOnView(GameView view) { // TODO Auto-generated method stub
	}

	@Override
	public void onRemovingOnView(GameView view) { // TODO Auto-generated method stub
	}
}