package players;

import trade.CargoBuilder;
import trade.Trade;
import trade.TradeBuilder;
import worlds.World;

class WebClient extends Player implements Playable
{
    public String setWorld( World w )
    {
        this.world = w;
        this.unloaded = false;
        return "\n  'destination': '" + w.name + "',\n"
              + "\n  'status': 'http://localhost:2244/v0/ship/" + this.playerID + "'\n"
              + "}\n";
    }

    public String youAreHere()
    {
        return "{ 'message': 'Welcome to " + world.name + " (" + world.uwp + "/" + world.sectorAbbreviation + " " + world.hex + ")',\n";
    }

    public String printDestinations( World[] worlds )
    {
        String out = "\n  'Destinations in range': \n  [";
        for (int i=0; i<worlds.length; i++)
        {
            out += "\n  ";
            if ( i>0 ) out += ",";
            else       out += " ";
            out += "{'" + worlds[i].toString() + "': 'http://localhost:2244/v0/jump/" + this.playerID + "/" + i + "' }";
        }
        out += "\n  ]\n}\n";
        return out;
    }

    public String loadShip()
    {
        String out = "";
        if ( this.unloaded  )
        {
            ship.getFreight().load(this);
            out = "\n  'freight': " + ship.getFreight().getCount() + ",";
            ship.getLowPassengers().load(this);
            out += "\n  'low passengers': " + ship.getLowPassengers().getCount() + ",";
            ship.getMidPassengers().load(this);
            out += "\n  'mid passengers': " + ship.getMidPassengers().getCount() + ",";
            ship.getHighPassengers().load(this);
            out += "\n  'high passengers': " + ship.getHighPassengers().getCount() + ",";
            ship.setCargo(CargoBuilder.buildCargo(world));
            out += "\n  'spec cargo price per ton': " + ship.getCargo().buyPrice + ",";
        }
        return out;
    }

    public String unloadShip()
    {
        String out = "";
        if ( !unloaded )
        {
            ship.getHighPassengers().unload(world);
            ship.getMidPassengers().unload(world);
            ship.getLowPassengers().unload(world);
            ship.getFreight().unload(world);
            Trade trade = TradeBuilder.buildTrade(ship.getCargo(), world);
            //out += "\nSpec Cargo sale price:   " + trade.getSalePrice();
            //out += "\nSpec Cargo origin price: " + ship.getCargo().buyPrice;
            int net = trade.getSalePrice() - ship.getCargo().buyPrice;
            /*
            if (net < (int) (Math.random() * 1000)) // simulate the time value of money, Monte Carlo style.
                out += "\n   Will not sell cargo.";
            else
                out += "\n   Net profit: Cr " + net + " per ton.";
            */
            unloaded = true;
        }
        return out;
    }
}
