import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {
            // Step 1: Initialize CloudSim
            int numUsers = 1; // number of cloud users
            Calendar calendar = Calendar.getInstance();
            boolean traceFlag = false; // trace events

            CloudSim.init(numUsers, calendar, traceFlag);

            // Step 2: Create Datacenter
            Datacenter datacenter = createDatacenter("Datacenter_1");

            // Step 3: Create Broker
            DatacenterBroker broker = new DatacenterBroker("Broker");
            int brokerId = broker.getId();

            // Step 4: Create VMs
            List<Vm> vmList = new ArrayList<>();
            Vm vm = new Vm(0, brokerId, 1000, 1, 1024, 10000, 1000, "Xen", new CloudletSchedulerTimeShared());
            vmList.add(vm);
            broker.submitVmList(vmList);

            // Step 5: Create Cloudlets
            List<Cloudlet> cloudletList = new ArrayList<>();
            UtilizationModel utilizationModel = new UtilizationModelFull();

            Cloudlet cloudlet = new Cloudlet(0, 40000, 1, 300, 300, utilizationModel, utilizationModel, utilizationModel);
            cloudlet.setUserId(brokerId);
            cloudletList.add(cloudlet);

            broker.submitCloudletList(cloudletList);

            // Step 6: Start Simulation
            CloudSim.startSimulation();

            // Step 7: Stop Simulation
            CloudSim.stopSimulation();

            // Step 8: Print results
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            for (Cloudlet cl : newList) {
                System.out.println("Cloudlet " + cl.getCloudletId() + " finished in VM " + cl.getVmId());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Datacenter createDatacenter(String name) throws Exception {
        List<Host> hostList = new ArrayList<>();

        // Create a machine with 2 CPUs
        List<Pe> peList = new ArrayList<>();
        peList.add(new Pe(0, new PeProvisionerSimple(1000)));

        Host host = new Host(
                0,
                new RamProvisionerSimple(2048),
                new BwProvisionerSimple(10000),
                1000000,
                peList,
                new VmSchedulerTimeShared(peList)
        );
        hostList.add(host);

        // Create Datacenter Characteristics
        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                "x86", "Linux", "Xen", hostList, 10.0, 3.0, 0.05, 0.1, 0.1);

        return new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), new ArrayList<Storage>(), 0);
    }
}
