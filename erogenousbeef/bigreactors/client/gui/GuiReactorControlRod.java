package erogenousbeef.bigreactors.client.gui;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import erogenousbeef.bigreactors.common.BigReactors;
import erogenousbeef.bigreactors.common.tileentity.TileEntityReactorControlRod;
import erogenousbeef.bigreactors.gui.controls.BeefGuiLabel;
import erogenousbeef.bigreactors.net.PacketWrapper;
import erogenousbeef.bigreactors.net.Packets;

public class GuiReactorControlRod extends BeefGuiBase {

	TileEntityReactorControlRod entity;
	
	BeefGuiLabel titleString;
	BeefGuiLabel heatString;
	BeefGuiLabel fuelString;
	BeefGuiLabel wasteString;
	BeefGuiLabel rodStatus;

	GuiButton assembleBtn;
	GuiButton rodInsertBtn;
	GuiButton rodRetractBtn;
	GuiButton dumpInteriorBtn;
	
	public GuiReactorControlRod(Container c, TileEntityReactorControlRod controlRod) {
		super(c);
		
		entity = controlRod;
	}
	
	@Override
	protected String getGuiBackground() {
		// TODO: Real gui?
		return BigReactors.GUI_DIRECTORY + "ReactorController.png";
	}

	@Override
	public void initGui() {
		super.initGui();

		int leftX = 4;
		int topY = 4;
		
		titleString = new BeefGuiLabel(this, "Reactor Control Rod", leftX, topY);
		topY += titleString.getHeight() + 8;
		
		heatString = new BeefGuiLabel(this, "Heat: ??? C", leftX, topY);
		topY += heatString.getHeight() + 8;
		
		fuelString = new BeefGuiLabel(this, "Fuel: ???? (??%)", leftX, topY);
		topY += fuelString.getHeight() + 8;

		wasteString = new BeefGuiLabel(this, "Waste: ???? (??%)", leftX, topY);
		topY += wasteString.getHeight() + 8;

		rodStatus = new BeefGuiLabel(this, "Control Rod: ???", leftX, topY);
		
		int btnLeftX = leftX + rodStatus.getWidth() + 16;
		rodRetractBtn = new GuiButton(0, guiLeft + btnLeftX, guiTop + topY - 6, 20, 20, "-");
		btnLeftX += 22;
		rodInsertBtn = new GuiButton(1, guiLeft + btnLeftX, guiTop + topY - 6, 20, 20, "+");
		
		topY += rodStatus.getHeight() + 8;
		topY += 10;
		
		assembleBtn = new GuiButton(2, guiLeft + leftX, guiTop + topY, 78, 20, "Assemble");
		dumpInteriorBtn = new GuiButton(3, guiLeft + leftX + 90, guiTop + topY, 78, 20, "Dump");
		
		
		registerControl(titleString);
		registerControl(heatString);
		registerControl(fuelString);
		registerControl(wasteString);
		registerControl(rodStatus);
		
		buttonList.add(rodRetractBtn);
		buttonList.add(rodInsertBtn);
		buttonList.add(assembleBtn);
		buttonList.add(dumpInteriorBtn);
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		
		heatString.setLabelText(String.format("Heat: %2.2f C", entity.getHeat()));
		fuelString.setLabelText(String.format("Fuel: %d (%2.1f%%)", entity.getFuelAmount(), ((float)entity.getFuelAmount() / (float)entity.getTotalContainedAmount())*100f));
		wasteString.setLabelText(String.format("Waste: %d (%2.1f%%)", entity.getWasteAmount(), ((float)entity.getWasteAmount() / (float)entity.getTotalContainedAmount())*100f));
		
		rodStatus.setLabelText(String.format("Control Rod: %2d%%", entity.getControlRodInsertion()));
		if(entity.isAssembled()) {
			assembleBtn.displayString = "Disassemble";
			rodInsertBtn.enabled = true;
			rodRetractBtn.enabled = true;
		}
		else {
			assembleBtn.displayString = "Assemble";
			rodInsertBtn.enabled = false;
			rodRetractBtn.enabled = false;
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		String btnCmd;
		
		switch(button.id) {
		case 0:
			btnCmd = "rodRetract";
			break;
		case 1:
			btnCmd = "rodInsert";
			break;
		case 2:
			btnCmd = "assemble";
			break;
		case 3:
		default:
			btnCmd = "dump";
			break;
		}
		
		PacketDispatcher.sendPacketToServer(PacketWrapper.createPacket(BigReactors.CHANNEL, Packets.SmallMachineButton,
				new Object[] { entity.xCoord, entity.yCoord, entity.zCoord, btnCmd }));
	}
}
